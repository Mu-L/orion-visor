import type { TableColumnData } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
    default: true,
  }, {
    title: '操作用户',
    dataIndex: 'username',
    slotName: 'username',
    width: 140,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '操作主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
    width: 180,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '操作类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 146,
    align: 'left',
    default: true,
  }, {
    title: '文件数量',
    dataIndex: 'fileCount',
    slotName: 'fileCount',
    align: 'left',
    width: 100,
    default: true,
  }, {
    title: '操作文件',
    dataIndex: 'paths',
    slotName: 'paths',
    align: 'left',
    minWidth: 238,
    default: true,
  }, {
    title: '执行结果',
    dataIndex: 'result',
    slotName: 'result',
    align: 'left',
    width: 88,
    default: true,
  }, {
    title: '留痕地址',
    dataIndex: 'address',
    slotName: 'address',
    width: 156,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '操作时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return (record.startTime && dateFormat(new Date(record.startTime)));
    },
    default: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 80,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
