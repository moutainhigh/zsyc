import Vue from "vue";
import Router from "vue-router";
const Home = () => import("@/views/Home");
const Datas = () => import("@/views/Datas");
const NewOrder = () => import("@/views/NewOrder");
const BookingOrder = () => import("@/views/BookingOrder");
const PayType = () => import("@/views/PayType");
const MemberList = () => import("@/views/MemberList");
const MemberGroup = () => import("@/views/MemberGroup");
const MemberLeve = () => import("@/views/MemberLeve");
const MemberCoupon = () => import("@/views/MemberCoupon");
const StoreManager = () => import("@/views/StoreManager");
const Followup = () => import("@/views/Followup");
const CustomerService = () => import("@/views/CustomerService");
const Sorting = () => import("@/views/Sorting");
const Production = () => import("@/views/Production");
const Sweep = () => import("@/views/Sweep");
const Test = () => import("@/views/Test");
const Goods = () => import("@/views/Goods");
const GoodsPriceManage = () => import("@/views/GoodsPriceManage");
const GoodsInsert = () => import("@/views/GoodsInsert");
const GoodsCategoryManage = () => import("@/views/GoodsCategoryManage");
const GoodsBrandList = () => import("@/views/GoodsBrandList");
const ModifyPrice = () => import("@/views/ModifyPrice");
const GoodsAttribute = () => import("@/views/GoodsAttribute");
const Classification = () => import("@/views/Classification");
Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    { path: "/", redirect: "/home" },
    {
      path: "/home",
      component: Home,
      redirect: "/datas",
      children: [
          { path: "/datas", component: Datas },
          { path: "/new_order", component: NewOrder},
          { path: "/booking_order", component: BookingOrder},
          { path: "/pay_type", component: PayType},
          { path: "/MemberList", component: MemberList },
          { path: "/MemberGroup", component: MemberGroup },
          { path: "/MemberLeve", component: MemberLeve },
          { path: "/MemberCoupon", component: MemberCoupon },
          { path: "/StoreManager", component: StoreManager },
          { path: "/Sorting", component: Sorting },
          { path: "/Followup", component: Followup },
          { path: "/Production", component: Production },
          { path: "/CustomerService", component: CustomerService },
          { path: "/Sweep", component: Sweep },
          { path: "/Test", component: Test },
          { path: "/Goods", component: Goods },
          { path: "/GoodsPriceManage", component: GoodsPriceManage },
          { path: "/GoodsInsert", component: GoodsInsert },
          { path: "/GoodsCategoryManage", component: GoodsCategoryManage },
          { path: "/GoodsBrandList", component: GoodsBrandList },
          { path: "/ModifyPrice", component: ModifyPrice },
          { path: "/GoodsAttribute", component: GoodsAttribute },
          { path: "/Classification", component: Classification }
          ]
    }
  ]
});
