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
        },
        exposer:function (seckillId) {
            return '/seckill/seckill/'+seckillId+'/exposer';
        },
        execution:function (seckillId,md5) {
            return '/seckill/seckill/'+seckillId+'/'+md5+'/execution';
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
                seckill.handleSecKill(seckillId,seckillBox);
            });
        }else{
            // 秒杀开始
            seckill.handleSecKill(seckillId,seckillBox);
        }
    },

    handleSecKill:function (seckillId,node) {
        // 获取秒杀地址
        // 所有控制节点的操作，在操作节点内容前，先隐藏
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

        // 获取秒杀地址
        $.post(
            seckill.URL.exposer(seckillId),
            {},
            function (result) {
                // 执行交互流程
                if(result && result['success']){
                    var exposer = result['data'];
                    // 判断是否开启秒杀
                    if(exposer['exposed']){
                        // 获取md5与url
                        var md5 = exposer['md5'];
                        var killUrl = seckill.URL.execution(seckillId,md5);

                        // $('#killBtn').click(),一直绑定事件,one只绑定一次
                        // 绑定一次点击事件,避免连续点击
                        $('#killBtn').one('click',function () {
                            // 绑定执行秒杀请求
                            // 先禁用按钮
                            $(this).addClass('disabled');
                            // 发送秒杀请求执行秒杀
                            $.post(killUrl,{},function (result) {
                                if(result && result['success']){
                                    var killResult = result['data'];
                                    var state = killResult['state'];
                                    var stateInfo = killResult['stateInfo'];
                                    // 显示结果
                                    node.html('<span class="label label-success">'+stateInfo+'</span>')
                                 }
                            });
                        });
                        node.show();
                    }
                    // 未开启秒杀
                    else{
                        var now = exposer['now'];
                        var start = exposer['start'];//start time
                        var end = exposer['end'];// end time

                        // 再次计算倒计时逻辑，纠正服务器与客户端之间的时间偏差
                        seckill.countdown(seckillId,now,start,end);
                    }
                }else{
                    console.log("result:"+result);
                }
            }
        );
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
