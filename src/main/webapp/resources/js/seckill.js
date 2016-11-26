/**
 * Created by oahnus on 2016/11/26.
 */
// 主要交互逻辑
// 模块化,避免代码过于混乱

var seckill = {
    // ajax用到的url
    URL : {
        now:function () {
            return '/seckill/seckill/time/now';
        }
    },

    // 验证手机号
    validatePhone : function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },

    // 时间判断
    countdown : function (seckillId,nowTime,startTime,endTime) {
        var seckillBox = $('#seckill-box');

        if(nowTime > endTime){
            seckillBox.html('秒杀结束');
        }else if(nowTime < startTime){
            seckillBox.html('秒杀未开始');

            var killTime = new Date(Number(startTime)+1000);
            seckillBox.countdown(killTime,function (event) {
                // 控制时间格式
                var format = event.strftime('秒杀倒计时:%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                // 时间完成后的回调函数
                // 获取秒杀地址
                seckill.handleSecKill();
            });
        }else{
            // 秒杀开始
            seckill.handleSecKill();

        }
    },

    handleSecKill:function () {

    },

    // 详情页逻辑
    detail : {
        // 详情页初始化
        init : function (params) {
            // 从cookie中读取手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            // 如果cookie中没有手机号
            if(!seckill.validatePhone(killPhone)){
                // 绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                // show 显示弹出层，backdrop禁止位置关闭，keyboard关闭键盘事件
                killPhoneModal.modal(
                    {
                        show:true,
                        backdrop:'static',
                        keyboard:false
                    }
                );

                // 事件绑定
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)){
                        // 手机号写入cookie
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        // 刷新页面
                        window.location.reload();
                    }else{
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }

            // 计时交互
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(
                seckill.URL.now(),
                {},
                function (result) {
                    if(result && result['success']){
                        var nowTime = result['data'];
                        // 时间判断
                        seckill.countdown(seckillId,nowTime,startTime,endTime);
                    }else{

                    }
                }
            );
        }
    }
};
