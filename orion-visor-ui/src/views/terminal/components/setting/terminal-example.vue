<template>
  <div class="terminal-example" ref="viewport" />
</template>

<script lang="ts">
  export default {
    name: 'terminalExample'
  };
</script>

<script lang="ts" setup>
  import type { TerminalThemeSchema } from '@/views/terminal/interfaces';
  import { Terminal } from '@xterm/xterm';
  import { markRaw, onMounted, onUnmounted, ref } from 'vue';

  const props = defineProps<{
    schema: TerminalThemeSchema | Record<string, any>;
  }>();

  const viewport = ref();
  const term = ref();

  onMounted(() => {
    const terminal = new Terminal({
      theme: { ...props.schema, cursor: props.schema.background },
      cols: 42,
      rows: 6,
      fontSize: 15,
      cursorInactiveStyle: 'none',
    });
    terminal.open(viewport.value);
    terminal.write(
      '[1;94m[root[0m@[1;96mOrionServer usr]#[0m\r\n' +
      'dr-xr-xr-x.  2 root root [0m[01;34mbin[0m\r\n' +
      'dr-xr-xr-x.  2 root root [01;34msbin[0m\r\n' +
      'drwxr-xr-x.  4 root root [01;34msrc[0m\r\n' +
      'lrwxrwxrwx.  1 root root [01;36mtmp[0m -> [30;42m../var/tmp[0m '
    );
    term.value = markRaw(terminal);
  });

  defineExpose({ term });

  onUnmounted(() => {
    term.value?.dispose();
  });

</script>

<style lang="less" scoped>
  .terminal-example {
    padding: 16px;
    width: 100%;
    height: 100%;
  }

  :deep(.xterm-viewport) {
    overflow: hidden !important;
  }
</style>
