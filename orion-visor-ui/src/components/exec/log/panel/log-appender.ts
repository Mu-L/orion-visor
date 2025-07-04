import type { ILogAppender, LogAppenderConfig, LogAppenderView } from '../const';
import { LogAppenderOptions } from '../const';
import type { XtermAddons } from '@/types/xterm';
import { openExecLogChannel } from '@/api/exec/exec-log';
import { getExecCommandLogTailToken } from '@/api/exec/exec-command-log';
import { getExecJobLogTailToken } from '@/api/exec/exec-job-log';
import { Message } from '@arco-design/web-vue';
import { useDebounceFn } from '@vueuse/core';
import { addEventListen, removeEventListen } from '@/utils/event';
import { copy as copyText } from '@/hooks/copy';
import { Terminal } from '@xterm/xterm';
import { FitAddon } from '@xterm/addon-fit';
import { SearchAddon } from '@xterm/addon-search';
import { WebLinksAddon } from '@xterm/addon-web-links';
import { CanvasAddon } from '@xterm/addon-canvas';
import { Unicode11Addon } from '@xterm/addon-unicode11';

// 执行日志 appender 实现
export default class LogAppender implements ILogAppender {

  private current: LogAppenderView;

  private client?: WebSocket;

  private readonly appenderViews: Record<string, LogAppenderView>;

  private readonly config: LogAppenderConfig;

  private keepAliveTask?: number;

  private readonly fitAllFn: () => void;

  constructor(config: LogAppenderConfig) {
    this.config = config;
    this.current = undefined as unknown as LogAppenderView;
    this.appenderViews = {};
    this.fitAllFn = useDebounceFn(this.fitAll, 300).bind(this);
  }

  // 初始化
  async init(configs: Array<LogAppenderView>) {
    // 初始化 appender
    await this.initAppender(configs);
    // 初始化 client
    await this.openClient();
  }

  // 初始化 appender
  async initAppender(configs: Array<LogAppenderView>) {
    // 打开 log-view
    for (let config of configs) {
      // 初始化 terminal
      const terminal = new Terminal({
        ...LogAppenderOptions,
        scrollback: this.config.scrollLines,
      });
      // 初始化快捷键
      this.initCustomKey(terminal);
      // 初始化插件
      const addons = this.initAddons(terminal);
      // 打开终端
      terminal.open(config.viewport);
      // 自适应
      addons.fit.fit();
      this.appenderViews[config.id] = {
        ...config,
        terminal,
        addons
      };
    }
    // 设置当前对象
    this.current = this.appenderViews[configs[0].id];
    // 注册自适应事件
    addEventListen(window, 'resize', this.fitAllFn);
  }

  // 初始化快捷键操作
  initCustomKey(terminal: Terminal) {
    terminal.attachCustomKeyEventHandler((e: KeyboardEvent) => {
      if (e.type !== 'keydown') {
        return true;
      }
      if (e.code === 'Enter') {
        // 新起一行
        e.preventDefault();
        terminal.write('\r\n');
      } else if (e.code === 'Home') {
        // 上移一页
        e.preventDefault();
        terminal.scrollPages(-1);
      } else if (e.code === 'ArrowUp') {
        // 上移
        e.preventDefault();
        terminal.scrollLines(-1);
      } else if (e.code === 'End') {
        // 下移一页
        e.preventDefault();
        terminal.scrollPages(1);
      } else if (e.code === 'ArrowDown') {
        // 下移
        e.preventDefault();
        terminal.scrollLines(1);
      } else if (e.ctrlKey && e.code === 'KeyA') {
        // 全选
        e.preventDefault();
        this.selectAll();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyC') {
        // 复制
        e.preventDefault();
        this.copy();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyF') {
        // 搜索
        e.preventDefault();
        this.current.openSearch();
        return false;
      } else if (e.ctrlKey && e.code === 'KeyL') {
        // 清空
        e.preventDefault();
        this.clear();
        return false;
      }
      return true;
    });
  }

  // 初始化插件
  initAddons(terminal: Terminal): XtermAddons {
    const fit = new FitAddon();
    const canvas = new CanvasAddon();
    const search = new SearchAddon();
    const weblink = new WebLinksAddon();
    const unicode = new Unicode11Addon();
    terminal.loadAddon(fit);
    terminal.loadAddon(canvas);
    terminal.loadAddon(search);
    terminal.loadAddon(weblink);
    terminal.loadAddon(unicode);
    terminal.unicode.activeVersion = '11';
    return {
      fit,
      canvas,
      search,
      weblink,
      unicode
    } as XtermAddons;
  }

  // 初始化 client
  async openClient() {
    let tokenMaker;
    // 获取 token
    if (this.config.type === 'BATCH') {
      // 获取批量执行日志 token
      tokenMaker = getExecCommandLogTailToken(this.config.id);
    } else {
      // 获取计划任务日志 token
      tokenMaker = getExecJobLogTailToken(this.config.id);
    }
    const { data } = await tokenMaker;
    // 打开会话
    try {
      this.client = await openExecLogChannel(data);
    } catch (e) {
      Message.error('连接失败');
      console.error('log error', e);
      return;
    }
    this.client.onclose = event => {
      console.warn('log close', event);
    };
    this.client.onmessage = this.processMessage.bind(this);
    // 注册持久化
    this.keepAliveTask = window.setInterval(() => {
      if (this.client?.readyState === WebSocket.OPEN) {
        this.client?.send('p');
      }
    }, 15000);
  }

  // 打开日志
  openLog(id: number): void {
    const view = this.appenderViews[id];
    if (!view || view.opened || this.client?.readyState !== WebSocket.OPEN) {
      return;
    }
    // 发送打开日志
    this.client?.send(id.toString());
    view.opened = true;
  }

  // 设置当前元素
  setCurrent(id: number): void {
    const view = this.appenderViews[id];
    if (!view) {
      return;
    }
    this.current = view;
    // 自适应
    view.addons.fit.fit();
    this.focus();
  }

  // 打开搜索
  openSearch() {
    this.current.openSearch();
  }

  // 查找关键字
  find(word: string, next: boolean, options: any) {
    if (next) {
      this.current.addons.search.findNext(word, options);
    } else {
      this.current.addons.search.findPrevious(word, options);
    }
  }

  // 去顶部
  toTop(): void {
    this.current.terminal.scrollToTop();
    this.focus();
  }

  // 去底部
  toBottom(): void {
    this.current.terminal.scrollToBottom();
    this.focus();
  }

  // 添加字体大小
  addFontSize(addSize: number): void {
    this.current.terminal.options['fontSize'] = this.current.terminal.options['fontSize'] as number + addSize;
    this.current.addons.fit.fit();
    this.focus();
  }

  // 复制
  copy(): void {
    copyText(this.current.terminal.getSelection(), true);
    this.focus();
  }

  // 复制全部
  copyAll(): void {
    this.selectAll();
    this.copy();
    this.current.terminal.clearSelection();
    this.focus();
  }

  // 选中全部
  selectAll(): void {
    this.current.terminal.selectAll();
    this.focus();
  }

  // 清空
  clear(): void {
    this.current.terminal.clear();
    this.current.terminal.clearSelection();
    this.focus();
  }

  // 聚焦
  focus(): void {
    this.current.terminal.focus();
  }

  // 自适应全部
  fitAll(): void {
    Object.values(this.appenderViews).forEach(s => {
      s.addons.fit.fit();
    });
  }

  // 关闭 client
  closeClient(): void {
    try {
      // 清理持久化
      clearInterval(this.keepAliveTask);
      // 关闭 ws
      if (this.client && this.client.readyState === WebSocket.OPEN) {
        this.client.close();
      }
    } catch (e) {
    }
  }

  // 关闭 view
  closeView(): void {
    // 移除自适应事件
    removeEventListen(window, 'resize', this.fitAllFn);
    // 关闭 terminal
    Object.values(this.appenderViews).forEach(s => {
      try {
        // 卸载插件
        Object.values(s.addons)
          .filter(Boolean)
          .forEach(s => s.dispose());
        // 卸载终端
        setTimeout(() => {
          s.terminal?.dispose();
        }, 300);
      } catch (e) {
        // 卸载可能会报错
      }
    });
  }

  // 关闭
  close(): void {
    this.closeClient();
    this.closeView();
  }

  // 处理消息
  processMessage({ data }: MessageEvent<string>) {
    // pong
    if (data === 'p') {
      return;
    }
    const separatorIndex = data.indexOf('|');
    const id = data.substring(0, separatorIndex);
    const text = data.substring(separatorIndex + 1, data.length);
    // 获取 view
    const view = this.appenderViews[id];
    if (!view) {
      return;
    }
    view.terminal.write(text);
  }

}
