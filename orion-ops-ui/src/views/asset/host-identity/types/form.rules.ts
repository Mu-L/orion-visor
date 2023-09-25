import { FieldRule } from '@arco-design/web-vue';

export const name = [{
  required: true,
  message: '请输入名称'
}, {
  maxLength: 64,
  message: '名称长度不能大于64位'
}] as FieldRule[];

export const username = [{
  required: true,
  message: '请输入用户名'
}, {
  maxLength: 128,
  message: '用户名长度不能大于128位'
}] as FieldRule[];

export const password = [{
  maxLength: 512,
  message: '用户密码长度不能大于512位'
}] as FieldRule[];

export default {
  name,
  username,
  password,
} as Record<string, FieldRule | FieldRule[]>;