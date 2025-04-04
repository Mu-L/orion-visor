import type { ISftpTransferManager, ITerminalPanelManager, ITerminalSessionManager, ITerminalTabManager } from '@/views/host/terminal/types/define';
import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
import type { TerminalTheme } from '@/api/asset/terminal';

export interface TerminalState {
  preference: TerminalPreference;
  layoutState: TerminalLayoutState;
  hosts: AuthorizedHostQueryResponse;
  tabManager: ITerminalTabManager;
  panelManager: ITerminalPanelManager;
  sessionManager: ITerminalSessionManager;
  transferManager: ISftpTransferManager;
}

// 终端配置
export interface TerminalPreference {
  newConnectionType: string;
  theme: TerminalTheme;
  displaySetting: TerminalDisplaySetting;
  actionBarSetting: TerminalActionBarSetting;
  rightMenuSetting: Array<string>,
  interactSetting: TerminalInteractSetting;
  pluginsSetting: TerminalPluginsSetting;
  sessionSetting: TerminalSessionSetting;
  shortcutSetting: TerminalShortcutSetting;
}

// 显示设置
export interface TerminalDisplaySetting {
  fontFamily?: string;
  fontSize?: number;
  lineHeight?: number;
  letterSpacing?: number;
  fontWeight?: string | number;
  fontWeightBold?: string | number;
  cursorStyle?: string;
  cursorBlink?: boolean;
}

// 操作栏设置
export interface TerminalActionBarSetting {
  connectStatus?: boolean;

  [key: string]: unknown;
}

// 交互设置
export interface TerminalInteractSetting {
  fastScrollModifier: boolean;
  altClickMovesCursor: boolean;
  rightClickSelectsWord: boolean;
  selectionChangeCopy: boolean;
  copyAutoTrim: boolean;
  pasteAutoTrim: boolean;
  rightClickPaste: boolean;
  enableRightClickMenu: boolean;
  enableBell: boolean;
  wordSeparator: string;
}

// 插件设置
export interface TerminalPluginsSetting {
  enableWeblinkPlugin: boolean;
  enableWebglPlugin: boolean;
  enableUnicodePlugin: boolean;
  enableImagePlugin: boolean;
}

// 会话设置
export interface TerminalSessionSetting {
  terminalEmulationType: string;
  scrollBackLine: number;
}

// 终端快捷键设置
export interface TerminalShortcutSetting {
  enabled: boolean;
  keys: Array<TerminalShortcutKey>;
}

// 终端快捷键
export interface ShortcutKey {
  ctrlKey: boolean;
  shiftKey: boolean;
  altKey: boolean;
  code: string;
}

// 终端快捷键
export interface TerminalShortcutKey extends ShortcutKey {
  item: string;
  enabled: boolean;
}

// 终端快捷键编辑
export interface TerminalShortcutKeyEditable extends TerminalShortcutKey {
  editable: boolean;
  content: string;
  type: number;
  shortcutKey?: string;
}

// 终端布局状态
export interface TerminalLayoutState {
  commandBar: boolean;
  fullscreen: boolean;
}
