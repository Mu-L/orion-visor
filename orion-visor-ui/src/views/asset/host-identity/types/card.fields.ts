import type { CardField, CardFieldConfig } from '@/types/card';
import { dateFormat } from '@/utils';

const fieldConfig = {
  rowGap: '10px',
  labelSpan: 8,
  minHeight: '22px',
  fields: [
    {
      label: 'id',
      dataIndex: 'id',
      slotName: 'id',
    }, {
      label: '用户名',
      dataIndex: 'username',
      slotName: 'username',
      ellipsis: true,
    }, {
      label: '类型',
      dataIndex: 'type',
      slotName: 'type',
    }, {
      label: '主机密钥',
      dataIndex: 'keyId',
      slotName: 'keyId',
      height: '24px',
    }, {
      label: '修改时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.updateTime));
      },
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
