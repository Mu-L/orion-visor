import type { SftpTransferItem } from '../types/terminal.type';
import { TransferStatus, TransferType } from '../types/terminal.const';
import { getFileName, openDownloadFile } from '@/utils/file';
import { saveAs } from 'file-saver';
import { getDownloadTransferUrl } from '@/api/asset/host-sftp';
import SftpTransferHandler from './sftp-transfer-handler';

// sftp 下载器实现
export default class SftpTransferDownloader extends SftpTransferHandler {

  constructor(item: SftpTransferItem, client: WebSocket) {
    super(TransferType.DOWNLOAD, item, client);
  }

  // 开始回调
  onStart(channelId: string, token: string) {
    super.onStart(channelId, token);
    // 获取下载 url
    const url = getDownloadTransferUrl(channelId, token);
    // 打开
    openDownloadFile(url);
  }

  // 完成回调
  onFinish() {
    super.onFinish();
    if (this.aborted) {
      // 中断则不触发下载
      return;
    }
    if (this.item.totalSize === 0) {
      // 空文件直接触发下载
      try {
        // 触发下载
        saveAs(new Blob([], {
          type: 'application/octet-stream'
        }), getFileName(this.item.name));
        this.item.status = TransferStatus.SUCCESS;
      } catch (e) {
        this.item.status = TransferStatus.ERROR;
        this.item.errorMessage = '保存失败';
      }
    } else {
      this.item.status = TransferStatus.SUCCESS;
    }
  }

}
