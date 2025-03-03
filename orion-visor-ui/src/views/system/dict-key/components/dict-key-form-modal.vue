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
        <!-- 配置项 -->
        <a-form-item field="keyName" label="配置项">
          <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
        </a-form-item>
        <!-- 配置值类型 -->
        <a-form-item field="valueType" label="配置值类型">
          <a-select v-model="formModel.valueType"
                    :options="toOptions(dictValueTypeKey)"
                    placeholder="请选择配置值类型" />
        </a-form-item>
        <!-- 配置描述 -->
        <a-form-item field="description" label="配置描述">
          <a-input v-model="formModel.description" placeholder="请输入配置描述" allow-clear />
        </a-form-item>
        <a-divider orientation="center" style="margin: 12px 0 26px 0;">额外参数定义</a-divider>
        <!-- 额外参数 -->
        <a-form-item v-for="(schema, index) in extraSchemaArr"
                     :key="index"
                     :field="`extra${index + 1}`"
                     :label="`额外参数 ${index + 1}`">
          <a-input-group style="width: 100%;">
            <!-- 参数类型 -->
            <a-select v-model="schema.type"
                      :options="toOptions(dictValueTypeKey)"
                      placeholder="类型"
                      :style="{ width: '35%' }" />
            <!-- 参数值 -->
            <a-input v-model="schema.name"
                     placeholder="参数名称"
                     :style="{ width: '65%' }" />
          </a-input-group>
          <!-- 操作按钮 -->
          <div class="extra-action">
            <!-- 删除 -->
            <a-button class="minus-icon-wrapper icon-button"
                      title="移除参数"
                      @click="removeExtraParam(index)">
              <icon-minus />
            </a-button>
          </div>
        </a-form-item>
        <!-- 参数操作 -->
        <a-space class="param-addition">
          <!-- 快捷定义 -->
          <a-tag v-for="definedExtraKey in definedExtraKeys"
                 color="arcoblue"
                 :title="`添加参数 ${definedExtraKey.name}`"
                 @click="addExtraParam(definedExtraKey.name, definedExtraKey.type)"
                 checkable
                 checked>
            {{ definedExtraKey.name }}
          </a-tag>
          <!-- 添加参数 -->
          <a-button title="添加参数"
                    style="width: 140px;"
                    @click="addExtraParam(undefined,undefined)"
                    long>
            <icon-plus />
          </a-button>
        </a-space>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'dictKeyFormModal'
  };
</script>

<script lang="ts" setup>
  import type { DictKeyUpdateRequest } from '@/api/system/dict-key';
  import type { ExtraParamType } from '../types/const';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import { createDictKey, updateDictKey } from '@/api/system/dict-key';
  import { definedExtraKeys, innerKeys, dictValueTypeKey, ValueType } from '../types/const';
  import { useDictStore } from '@/store';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): DictKeyUpdateRequest => {
    return {
      id: undefined,
      keyName: undefined,
      valueType: ValueType.INTEGER,
      extraSchema: undefined,
      description: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<DictKeyUpdateRequest>({});
  const extraSchemaArr = ref<Array<ExtraParamType>>([]);

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加字典配置项';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改字典配置项';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
    if (record.extraSchema) {
      extraSchemaArr.value = JSON.parse(record.extraSchema);
    } else {
      extraSchemaArr.value = [];
    }
  };

  defineExpose({ openAdd, openUpdate });

  // 添加额外参数
  const addExtraParam = (name: string | undefined, type: any) => {
    if (name && extraSchemaArr.value.findIndex(v => v.name === name) != -1) {
      return;
    }
    extraSchemaArr.value.push({
      name: name as string,
      type: type || ValueType.STRING
    });
  };

  // 移除额外参数
  const removeExtraParam = (index: number) => {
    extraSchemaArr.value.splice(index, 1);
  };

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 验证额外参数
      if (extraSchemaArr.value) {
        for (let i = 0; i < extraSchemaArr.value.length; i++) {
          const extraSchema = extraSchemaArr.value[i];
          // 为空
          if (!extraSchema.name) {
            formRef.value.setFields({
              [`extra${i + 1}`]: {
                status: 'error',
                message: '参数不能为空'
              }
            });
            return false;
          }
          // 不合法
          if (!new RegExp(/^[a-zA-Z0-9_]{2,32}$/).test(extraSchema.name as string)) {
            formRef.value.setFields({
              [`extra${i + 1}`]: {
                status: 'error',
                message: '配置项需要为 2-32 位的数字,字母或下滑线'
              }
            });
            return false;
          }
          // 不能为内置参数
          if (innerKeys.includes(extraSchema.name)) {
            formRef.value.setFields({
              [`extra${i + 1}`]: {
                status: 'error',
                message: '参数不能为 name label value'
              }
            });
            return false;
          }
        }
      }
      // 保存
      if (isAddHandle.value) {
        // 新增
        await createDictKey({ ...formModel.value, extraSchema: JSON.stringify(extraSchemaArr.value) });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateDictKey({ ...formModel.value, extraSchema: JSON.stringify(extraSchemaArr.value) });
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

  .minus-icon-wrapper {
    width: 32px;
    height: 32px;
    margin-left: 8px;
    font-size: 18px;
  }

  .param-addition {
    margin-bottom: 20px;
    justify-content: flex-end;
    cursor: pointer;
    font-size: 18px;
    user-select: none;
  }
</style>
