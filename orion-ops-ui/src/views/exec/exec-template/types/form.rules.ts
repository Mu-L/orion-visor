import type { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入模板名称'
}, {
  maxLength: 64,
  message: '模板名称长度不能大于64位'
}] as FieldRule[];

export const command = [{
  required: true,
  message: '请输入模板命令'
}] as FieldRule[];

export const timeout = [{
  required: true,
  message: '请输入超时时间'
}] as FieldRule[];

export default {
  name,
  command,
  timeout,
} as Record<string, FieldRule | FieldRule[]>;