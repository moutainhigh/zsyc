import Vue from "vue";
import App from "./App.vue";
import elementUi from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import axios from "axios";
import router from "./router";
import store from "./store";
import "./assets/css/global.css";


Vue.use(elementUi);
Vue.prototype.$http = axios;
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
