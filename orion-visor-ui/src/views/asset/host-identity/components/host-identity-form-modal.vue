<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           :title="title"
           :top="80"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear />
        </a-form-item>
        <!-- 类型 -->
        <a-form-item field="type" label="类型">
          <a-radio-group v-model="formModel.type"
                         type="button"
                         class="usn"
                         :options="toRadioOptions(identityTypeKey)" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="formModel.username"
                   placeholder="请输入用户名"
                   allow-clear />
        </a-form-item>
        <!-- 用户密码 -->
        <a-form-item v-if="formModel.type === IdentityType.PASSWORD"
                     field="password"
                     label="用户密码"
                     :rules="passwordRules">
          <a-input-password v-model="formModel.password"
                            :disabled="!isAddHandle && !formModel.useNewPassword"
                            :class="[isAddHandle ? 'password-input-full' : 'password-input']"
                            placeholder="请输入用户密码" />
          <a-switch v-model="formModel.useNewPassword"
                    v-if="!isAddHandle"
                    class="password-switch"
                    type="round"
                    checked-text="使用新密码"
                    unchecked-text="使用原密码" />
        </a-form-item>
        <!-- 主机密钥 -->
        <a-form-item v-if="formModel.type === IdentityType.KEY"
                     field="keyId"
                     label="主机密钥"
                     :hide-asterisk="true">
          <host-key-selector v-model="formModel.keyId" />
        </a-form-item>
        <!-- 描述 -->
        <a-form-item field="description" label="描述">
          <a-textarea v-model="formModel.description"
                      placeholder="请输入描述"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'hostIdentityFormModal'
  };
</script>

<script lang="ts" setup>
  import type { HostIdentityUpdateRequest } from '@/api/asset/host-identity';
  import type { FieldRule } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHostIdentity, updateHostIdentity } from '@/api/asset/host-identity';
  import { Message } from '@arco-design/web-vue';
  import { IdentityType, identityTypeKey } from '../types/const';
  import { useDictStore } from '@/store';
  import { encrypt } from '@/utils/rsa';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';

  const { toRadioOptions, getDictValue } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): HostIdentityUpdateRequest => {
    return {
      id: undefined,
      type: IdentityType.PASSWORD,
      name: undefined,
      username: undefined,
      password: undefined,
      useNewPassword: false,
      keyId: undefined,
      description: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<HostIdentityUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机身份';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改主机身份';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ openAdd, openUpdate });

  // 密码认证
  const passwordRules = [{
    validator: (value, cb) => {
      if (value && value.length > 512) {
        cb('密码长度不能大于512位');
        return;
      }
      if (formModel.value.useNewPassword && !value) {
        cb('请输入密码');
        return;
      }
    }
  }] as FieldRule[];

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 认证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 加密参数
      let password = undefined;
      try {
        password = await encrypt(formModel.value.password);
      } catch (e) {
        return false;
      }
      if (isAddHandle.value) {
        // 新增
        await createHostIdentity({ ...formModel.value, password });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHostIdentity({ ...formModel.value, password });
        Message.success('修改成功');
        emits('updated');
      }
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  @switch-width: 94px;

  .password-input {
    width: calc(100% - @switch-width);
  }

  .password-input-full {
    width: 100%;
  }

  .password-switch {
    width: @switch-width;
    margin-left: 16px;
  }
</style>
