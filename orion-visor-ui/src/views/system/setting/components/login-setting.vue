<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">登录设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 允许多端登录 -->
      <a-form-item field="login_allowMultiDevice"
                   label="允许多端登录"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting.login_allowMultiDevice"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后一个账号可以多个设备同时登录
        </template>
      </a-form-item>
      <!-- 允许凭证续签 -->
      <a-form-item field="login_allowRefresh"
                   label="允许凭证续签"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting.login_allowRefresh"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后当凭证即将过期时，系统会自动续签
        </template>
      </a-form-item>
      <!-- 登录失败锁定 -->
      <a-form-item field="login_loginFailedLock"
                   label="登录失败锁定"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting.login_loginFailedLock"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后当登录失败次数达到阈值时账号会自动锁定
        </template>
      </a-form-item>
      <!-- 登录失败发信 -->
      <a-form-item field="login_loginFailedSend"
                   label="登录失败发信"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting.login_loginFailedSend"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后当登录失败次数达到阈值时将发送站内信
        </template>
      </a-form-item>
      <!-- 凭证有效期 -->
      <a-form-item field="login_loginSessionTime"
                   label="凭证有效期"
                   :rules="[{required: true, message: '请输入凭证有效期'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_loginSessionTime"
                        class="input-wrapper"
                        :min="1"
                        :max="99999"
                        placeholder="请输入凭证有效期"
                        allow-clear
                        hide-button>
          <template #suffix>
            分
          </template>
        </a-input-number>
        <template #extra>
          设置登录凭证有效期时长(分)
        </template>
      </a-form-item>
      <!-- 凭证续签间隔 -->
      <a-form-item field="login_refreshInterval"
                   label="凭证续签间隔"
                   :rules="[{required: true, message: '请输入凭证续签间隔时间'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_refreshInterval"
                        class="input-wrapper"
                        :min="1"
                        :max="99999"
                        placeholder="请输入凭证续签间隔时间"
                        allow-clear
                        hide-button>
          <template #suffix>
            分
          </template>
        </a-input-number>
        <template #extra>
          当登录凭证过期但未超过续签间隔时，系统会自动续签
        </template>
      </a-form-item>
      <!-- 凭证续签最大次数 -->
      <a-form-item field="login_maxRefreshCount"
                   label="凭证续签最大次数"
                   :rules="[{required: true, message: '请输入凭证续签最大次数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_maxRefreshCount"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入凭证续签最大次数"
                        allow-clear
                        hide-button />
        <template #extra>
          凭证续签的最大次数
        </template>
      </a-form-item>
      <!-- 登录失败锁定阈值 -->
      <a-form-item field="login_loginFailedLockThreshold"
                   label="登录失败锁定阈值"
                   :rules="[{required: true, message: '请输入登录失败锁定阈值'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_loginFailedLockThreshold"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入登录失败锁定阈值"
                        allow-clear
                        hide-button />
        <template #extra>
          登录失败次数到达该值时账号会自动锁定
        </template>
      </a-form-item>
      <!-- 登录失败锁定时间 -->
      <a-form-item field="login_loginFailedLockTime"
                   label="登录失败锁定时间"
                   :rules="[{required: true, message: '请输入登录失败锁定时间'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_loginFailedLockTime"
                        class="input-wrapper"
                        :min="0"
                        :max="999999"
                        placeholder="请输入登录失败锁定时间"
                        allow-clear
                        hide-button>
          <template #suffix>
            分
          </template>
        </a-input-number>
        <template #extra>
          登录失败多次后账号锁定的时间(分)
        </template>
      </a-form-item>
      <!-- 登录失败发信阈值 -->
      <a-form-item field="login_loginFailedSendThreshold"
                   label="登录失败发信阈值"
                   :rules="[{required: true, message: '请输入登录失败发信阈值'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.login_loginFailedSendThreshold"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入登录失败发信阈值"
                        allow-clear
                        hide-button />
        <template #extra>
          登录失败次数到达该值时系统将发送站内信
        </template>
      </a-form-item>
      <!-- 按钮 -->
      <a-form-item v-permission="['infra:system-setting:update']">
        <!-- 保存 -->
        <a-button type="primary"
                  size="small"
                  @click="save">
          保存
        </a-button>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'loginSetting',
  };
</script>

<script lang="ts" setup>
  import type { LoginSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { toAnonymousNumber } from '@/utils';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<LoginSetting>({} as LoginSetting);

  // 保存
  const save = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return false;
    }
    setLoading(true);
    try {
      await updateSystemSettingBatch({
        type: SystemSettingTypes.LOGIN,
        settings: setting.value
      });
      Message.success('修改成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载配置
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemSetting(SystemSettingTypes.LOGIN);
      setting.value = {
        ...data,
        login_loginSessionTime: toAnonymousNumber(data.login_loginSessionTime),
        login_maxRefreshCount: toAnonymousNumber(data.login_maxRefreshCount),
        login_refreshInterval: toAnonymousNumber(data.login_refreshInterval),
        login_loginFailedLockThreshold: toAnonymousNumber(data.login_loginFailedLockThreshold),
        login_loginFailedLockTime: toAnonymousNumber(data.login_loginFailedLockTime),
        login_loginFailedSendThreshold: toAnonymousNumber(data.login_loginFailedSendThreshold),
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .input-wrapper {
    width: 368px;
  }
</style>
