<script setup lang="ts">
import { PipelineType } from "@/types/PipelineTypes";
import PvSelect from "@/components/common/pv-select.vue";
import PvSlider from "@/components/common/pv-slider.vue";
import PvSwitch from "@/components/common/pv-switch.vue";
import { computed, getCurrentInstance } from "vue";
import { useStateStore } from "@/stores/StateStore";
import type { ActivePipelineSettings } from "@/types/PipelineTypes";
import { useCameraSettingsStore } from "@/stores/settings/CameraSettingsStore";

// TODO fix pipeline typing in order to fix this, the store settings call should be able to infer that only valid pipeline type settings are exposed based on pre-checks for the entire config section
// Defer reference to store access method
const currentPipelineSettings = computed<ActivePipelineSettings>(
  () => useCameraSettingsStore().currentPipelineSettings
);

const interactiveCols = computed(() =>
  (getCurrentInstance()?.proxy.$vuetify.breakpoint.mdAndDown || false) &&
  (!useStateStore().sidebarFolded || useCameraSettingsStore().isDriverMode)
    ? 9
    : 8
);
</script>

<template>
  <div v-if="currentPipelineSettings.pipelineType === PipelineType.CustomTest">
    <pv-slider
      v-model="currentPipelineSettings.test1"
      class="pt-2"
      :slider-cols="interactiveCols"
      label="Test 1"
      tooltip="Gaussian blur added to the image, high FPS cost for slightly decreased noise"
      :min="0"
      :max="10"
      :step="1"
      @input="(value) => useCameraSettingsStore().changeCurrentPipelineSetting({ test1: value }, false)"
    />
    <pv-slider
      v-model="currentPipelineSettings.test2"
      class="pt-2"
      :slider-cols="interactiveCols"
      label="Test 2"
      tooltip="Gaussian blur added to the image, high FPS cost for slightly decreased noise"
      :min="0"
      :max="10"
      :step="1"
      @input="(value) => useCameraSettingsStore().changeCurrentPipelineSetting({ test2: value }, false)"
  />
  <pv-slider
        v-model="currentPipelineSettings.test3"
        class="pt-2"
        :slider-cols="interactiveCols"
        label="Test 3"
        tooltip="Gaussian blur added to the image, high FPS cost for slightly decreased noise"
        :min="0"
        :max="10"
        :step="1"
        @input="(value) => useCameraSettingsStore().changeCurrentPipelineSetting({ test3: value }, false)"
    />
  </div>
</template>
