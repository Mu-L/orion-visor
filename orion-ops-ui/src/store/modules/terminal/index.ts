import type { TabItem, TerminalDisplaySetting, TerminalPreference, TerminalState, TerminalThemeSchema } from './types';
import type { HostQueryResponse } from '@/api/asset/host';
import { defineStore } from 'pinia';
import { getPreference, updatePreference } from '@/api/user/preference';
import { Message } from '@arco-design/web-vue';
import { useDark } from '@vueuse/core';
import { DEFAULT_SCHEMA } from '@/views/host-ops/terminal/types/terminal.theme';
import { InnerTabs } from '@/views/host-ops/terminal/types/terminal.const';

// 暗色主题
export const DarkTheme = {
  DARK: 'dark',
  LIGHT: 'light',
  AUTO: 'auto'
};

export default defineStore('terminal', {
  state: (): TerminalState => ({
    isDarkTheme: useDark({
      selector: 'body',
      attribute: 'terminal-theme',
      valueDark: DarkTheme.DARK,
      valueLight: DarkTheme.LIGHT,
      initialValue: DarkTheme.DARK as any,
      storageKey: null
    }),
    preference: {
      darkTheme: 'auto',
      newConnectionType: 'group',
      displaySetting: {} as TerminalDisplaySetting,
      themeSchema: {} as TerminalThemeSchema
    },
    tabs: {
      active: InnerTabs.NEW_CONNECTION.key,
      items: [InnerTabs.NEW_CONNECTION, InnerTabs.VIEW_SETTING]
    }
  }),

  actions: {
    // 加载终端偏好
    async fetchPreference() {
      try {
        const { data } = await getPreference<TerminalPreference>('TERMINAL');
        // 设置默认终端主题
        if (!data.themeSchema?.name) {
          data.themeSchema = DEFAULT_SCHEMA;
        }
        this.preference = data;
        // 设置暗色主题
        const userDarkTheme = data.darkTheme;
        if (userDarkTheme === DarkTheme.AUTO) {
          this.isDarkTheme = data.themeSchema?.dark === true;
        } else {
          this.isDarkTheme = userDarkTheme === DarkTheme.DARK;
        }
      } catch (e) {
        Message.error('配置加载失败');
      }
    },

    // 修改暗色主题
    async changeDarkTheme(darkTheme: string) {
      this.preference.darkTheme = darkTheme;
      if (darkTheme === DarkTheme.DARK) {
        // 暗色
        this.isDarkTheme = true;
      } else if (darkTheme === DarkTheme.LIGHT) {
        // 亮色
        this.isDarkTheme = false;
      } else if (darkTheme === DarkTheme.AUTO) {
        // 自动配色
        this.isDarkTheme = this.preference.themeSchema.dark;
      }
      // 同步配置
      await this.updateTerminalPreference('darkTheme', darkTheme);
    },

    // 修改显示配置
    async changeDisplaySetting(displaySetting: TerminalDisplaySetting) {
      this.preference.displaySetting = displaySetting;
      // 同步配置
      await this.updateTerminalPreference('displaySetting', displaySetting);
    },

    // 选择终端主题
    async changeThemeSchema(themeSchema: TerminalThemeSchema) {
      this.preference.themeSchema = themeSchema;
      // 切换主题配色
      if (this.preference.darkTheme === DarkTheme.AUTO) {
        this.isDarkTheme = themeSchema.dark;
      }
      // 同步配置
      await this.updateTerminalPreference('themeSchema', themeSchema);
    },

    // 切换新建连接类型
    async changeNewConnectionType(newConnectionType: string) {
      this.preference.newConnectionType = newConnectionType;
      // 同步配置
      await this.updateTerminalPreference('newConnectionType', newConnectionType);
    },

    // 更新终端偏好
    async updateTerminalPreference(item: string, value: any) {
      try {
        // 修改配置
        await updatePreference({
          type: 'TERMINAL',
          item,
          value
        });
      } catch (e) {
        Message.error('同步失败');
      }
    },

    // 点击 tab
    clickTab(key: string) {
      this.tabs.active = key;
    },

    // 删除 tab
    deleteTab(key: string) {
      const tabIndex = this.tabs.items.findIndex(s => s.key === key);
      this.tabs.items.splice(tabIndex, 1);
      if (key === this.tabs.active && this.tabs.items.length !== 0) {
        // 切换为前一个 tab
        this.tabs.active = this.tabs.items[Math.max(tabIndex - 1, 0)].key;
      }
    },

    // 切换 tab
    switchTab(tab: TabItem) {
      // 不存在则创建tab
      if (!this.tabs.items.find(s => s.key === tab.key)) {
        this.tabs.items.push(tab);
      }
      this.tabs.active = tab.key;
    },

    // 打开终端
    openTerminal(record: HostQueryResponse) {
      console.log(record);
    }

  },

});
