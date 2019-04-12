var tt = new Vue({
    el : '#pp',
    data : {
        a : 1,
        b:100,
        records:[]
    },
    filters : {
        make1 : function (a) {
            return a + '2111';
        }
    },
    methods:{
        make : function (a) {
            return a + '2111';
        },
        add:function () {
            var _this = this;
            _this.a = _this.a * 2;
        }
    }
});