<template>
  <div class="p-d-flex p-flex-column">
    <!-- Mini Donught Charts -->
    <div class="p-d-flex p-flex-row p-jc-start">
      <div class="p-d-flex p-flex-column m-mini-chart-container">
        <div class="m-chart-title">Biểu đồ nhập vật tư theo quý {{year}}</div>
        <div class="p-d-flex p-flex-row p-ai-end">
          <Chart
            :height="30"
            :width="30"
            type="doughnut"
            :data="chartData.byReceipt"
            :options="miniDonutOptions"
            style="width: 100px; height: 100px"
          />
          <Legend
            class="p-mb-3 p-ml-2"
            :legends="chartData.byReceiptLegendData"
            direction="row"
          ></Legend>
        </div>
      </div>

      <div class="p-d-flex p-flex-column m-mini-chart-container p-ml-4">
        <div class="m-chart-title">Biểu đồ xuất vật tư theo quý {{year}}</div>
        <div class="p-d-flex p-flex-row p-ai-end">
          <Chart
            :height="100"
            :width="100"
            type="doughnut"
            :data="chartData.byDeliveryBill"
            :options="miniDonutOptions"
            style="width: 100px; height: 100px"
          />
          <Legend
            class="p-mb-3 p-ml-2"
            :legends="chartData.byDeliveryBillLegendData"
          ></Legend>
        </div>
      </div>
    </div>

    <!-- Bar Chart for Daily sale -->
    <div class="p-d-flex p-flex-column p-mt-4 m-chart-container">
      <div class="m-chart-title">Biểu đồ vật tư nhập xuất năm {{year}} </div>
      <Chart
        :height="130"
        :width="1000"
        type="bar"
        :data="chartData.suppliesStats"
      />
    </div>

    <!-- Bar Chart for Daily b-->
    <div class="p-d-flex p-flex-column p-mt-4 m-chart-container">
      <div class="m-chart-title">Biểu đồ tổng hợp xuất nhập tồn theo tháng năm {{year}}</div>
      <Chart
        :height="130"
        :width="1000"
        type="bar"
        :data="chartData.monthStats"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import { ref, reactive, onMounted, defineComponent } from "vue";
import {
  timeChartOptions as defaultTimeChartOptions,
  miniDonutOptions as defaultMiniDonutOptions,
} from "@/shared/chart-options";
import StatsApi from "@/api/stats-api"; // eslint-disable-line import/no-cycle
import Legend from "@/components/Legend.vue";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const year = ref(new Date().getFullYear());
    const timeChartOptions = defaultTimeChartOptions;
    const miniDonutOptions = defaultMiniDonutOptions;
    const chartData = reactive({
      suppliesStats: {},
      monthStats: {},
      byReceipt: {},
      byDeliveryBill: {},
      byReceiptLegendData: {},
      byDeliveryBillLegendData: {},
    });

    const getByReceipt = async () => {
      try {
        const resp = await StatsApi.getStats("by-receipt");
        const byReceipt: Record<string, number> = {};
        resp.data.list.forEach((dataRow: Record<string, string | number>) => {
          byReceipt[dataRow.quarter] = (
            dataRow.sumAmount ? dataRow.sumAmount : 0
          ) as number;
        });
        chartData.byReceipt = {
          labels: ["Quý I", "Quý II", "Quý III", "Quý IV"],
          datasets: [
            {
              data: [
                byReceipt["1"],
                byReceipt["2"],
                byReceipt["3"],
                byReceipt["4"],
              ],
              backgroundColor: ["#23A1FC", "orangered", "#A5BE00", "#FFB41E"],
              borderWidth: 0,
            },
          ],
        };
        chartData.byReceiptLegendData = [
          { label: "Quý I", value: byReceipt["1"], color: "#23A1FC" },
          { label: "Quý II", value: byReceipt["2"], color: "orangered" },
          { label: "Quý III", value: byReceipt["3"], color: "#A5BE00" },
          { label: "Quý IV", value: byReceipt["4"], color: "#FFB41E" },
        ];
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
      }
    };

    const getByPaymenType = async () => {
      try {
        const resp = await StatsApi.getStats("by-delivery-bill");
        const byDeliveryBill: Record<string, number> = {};
        resp.data.list.forEach((dataRow: Record<string, string | number>) => {
          byDeliveryBill[dataRow.quarter] = (
            dataRow.sumAmount ? dataRow.sumAmount : 0
          ) as number;
        });
        chartData.byDeliveryBill = {
          labels: ["Quý I", "Quý II", "Quý III", "Quý IV"],
          datasets: [
            {
              data: [
                byDeliveryBill["1"],
                byDeliveryBill["2"],
                byDeliveryBill["3"],
                byDeliveryBill["4"],
              ],
              backgroundColor: ["#23A1FC", "orangered", "#A5BE00", "#FFB41E"],
              borderWidth: 0,
            },
          ],
        };
        chartData.byDeliveryBillLegendData = [
          { label: "Quý I", value: byDeliveryBill["1"], color: "#23A1FC" },
          { label: "Quý II", value: byDeliveryBill["2"], color: "orangered" },
          { label: "Quý III", value: byDeliveryBill["3"], color: "#A5BE00" },
          { label: "Quý IV", value: byDeliveryBill["4"], color: "#FFB41E" },
        ];
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
      }
    };

    const getSuppliesStats = async () => {
      try {
        const resp = await StatsApi.getSuppliesStats();
        const dateLabels: string[] = [];
        const saleAmounts: number[] = [];
        const discounts: number[] = [];
        resp.data.list.forEach((dataRow: Record<string, string | unknown>) => {
          dateLabels.push(dataRow.suppliesName as string);
          saleAmounts.push(dataRow.sumReceipt as number);
          discounts.push(dataRow.sumDeliveryBill as number);
        });

        chartData.suppliesStats = {
          labels: dateLabels, // Labels should be Date objects
          datasets: [
            {
              label: 'Nhập kho',
              data: saleAmounts,
              borderWidth: 0,
              backgroundColor: '#23A1FC',
              pointRadius: 0,
              pointHitRadius: 10,
              pointHoverRadius: 3,
              pointHoverBorderWidth: 0,
            },
            {
              label: 'Xuất kho',
              data: discounts,
              borderWidth: 0,
              backgroundColor: '#FFA726',
              pointRadius: 0,
              pointHitRadius: 10,
              pointHoverRadius: 3,
              pointHoverBorderWidth: 0,
            },
          ],
        };
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
      }
    };

    const getMonthStats = async () => {
      try {
        const resp = await StatsApi.getMonthStats();
        const dateLabels: string[] = [];
        const sumReceipt: number[] = [];
        const sumDeliveryBill: number[] = [];
        const sumInventory: number[] = [];
        resp.data.list.forEach((dataRow: Record<string, string | unknown>) => {
          dateLabels.push(dataRow.month as string);
          sumReceipt.push(dataRow.sumReceipt as number);
          sumDeliveryBill.push(dataRow.sumDeliveryBill as number);
          sumInventory.push(dataRow.sumInventory as number);
        });

        chartData.monthStats = {
          labels: dateLabels, // Labels should be Date objects
          datasets: [
            {
              label: "Nhập",
              data: sumReceipt,
              borderWidth: 0,
              backgroundColor: "#23A1FC",
              pointRadius: 0,
              pointHitRadius: 10,
              pointHoverRadius: 3,
              pointHoverBorderWidth: 0,
            },
            {
              label: "Xuất",
              data: sumDeliveryBill,
              borderWidth: 0,
              backgroundColor: "#FFA726",
              pointRadius: 0,
              pointHitRadius: 10,
              pointHoverRadius: 3,
              pointHoverBorderWidth: 0,
            },
            {
              label: "Tồn",
              data: sumInventory,
              borderWidth: 0,
              backgroundColor: "#A5BE00",
              pointRadius: 0,
              pointHitRadius: 10,
              pointHoverRadius: 3,
              pointHoverBorderWidth: 0,
            },
          ],
        };
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
      }
    };

    const refreshAllChartData = () => {
      getSuppliesStats();
      getMonthStats();
      getByReceipt();
      getByPaymenType();
    };

    onMounted(() => {
      refreshAllChartData();
    });

    return {
      isLoading,
      miniDonutOptions,
      timeChartOptions,
      chartData,
      year
    };
  },
  components: {
    Legend,
  },
});
</script>
<style lang="scss" scoped>
.m-chart-container {
  max-width: 70rem;
}

.m-mini-chart-container {
  min-width: 26rem;
}

.m-chart-container,
.m-mini-chart-container {
  padding: 12px;
  background-color: #fff;
  border: 1px solid #aaa;
  border-top-color: #333;
  border-top-width: 4px;
}
.m-chart-title {
  color: #333;
  display: inline-block;
  font-size: 0.75rem;
  line-height: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}
</style>
