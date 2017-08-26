/**
 * Created by jason on 2015/1/30.
 * @author jason
 *
 *
 *
 */
ecss.alert=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">违规列表</li>'
                    +'</ol>'
                +'</div>'
                +'<div id="portlet" ></div>'
            +'</div>',
            body:String()
                +'<div class="tablecontent">'
                    +'<div class="buttons">'
                        +'<button class="btn btn-warning btn-sm" id="send">推送</button>'
                        +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                    +'</div>'
                    +'<table class="table table-hover table-bordered" id="DTalert"></table>'
                +'</div>',

            tableOption:{
                "ajax": { "url": "getallalerts.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"机构","data": "first" ,"render":function ( data , type, full) {
                        return (full.first?full.first:'全校')+(full.second?('-'+full.second):'')+(full.third?('-'+full.third):'');
                    }},
                    { title:"日期单位","data": "timeunit" ,"render":function ( data , type, full){
                        if(data=='year') return '年';
                        else
                        if(data=='month') return '月';
                        else
                        if(data=='day') return '日';
                    }},
                    { title:"日期","data": "datetime" ,"render":function ( data , type, full){
                        if(full.timeunit=='year') return moment(data).format('YYYY');
                        else
                        if(full.timeunit=='month') return moment(data).format('YYYY/MM');
                        else
                        if(full.timeunit=='day') return moment(data).format('YYYY/MM/DD');
                    }},
                    { title:"能耗分项","data": "energyitem" },
                    { title:"额定值","data": "value" },
                    { title:"使用值","data": "usage" },
                    { title:"生成时间","data": "alerttime"},
                    { title:"报警状态","data": "status",
                        "render":function ( data , type, full){
                        if(data==0) return '未报警';
                        else
                        if(data==1) return '80%提醒';
                        else
                        if(data==2) return '超额报警';
                    }}
                ]
            },

            formOption:[
                [{label:'label',text:'提醒方式',size:3},{label:'input',type:'checkbox',name:'alerttype',options:[{value:'email',text:'邮箱'},{value:'message',text:'短信'}],size:8,valid:{}}],
                [{label:'label',text:'发送人',size:3},{label:'select',name:'users',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'邮箱',size:3},{label:'input',type:'text',name:'email',size:8,valid:{}}],
                [{label:'label',text:'信息',size:3},{label:'input',type:'text',name:'phone',size:8,valid:{}}]
            ]
        },
        modelMap={
            modeluser:new ecss.tools.makeModel(ecss.model.user.user),
            model:new ecss.tools.makeModel(ecss.model.alert.alert),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,   sendAlert, deleteAlert,changeTable,equipSend,equipDelete,baoguanSend,baoguanDelete,setInfo,addOptions;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'违规用能列表','green','',configMap.body),
            $dtalert    :new ecss.tools.makeTable($container.find('#DTalert'),configMap.tableOption),
            $equipportlet    :new ecss.tools.makePortlet($container.find('#equipportlet'),'表计异常列表','green','',configMap.equipbody),
            $dtequipalert    :new ecss.tools.makeTable($container.find('#DTequipalert'),configMap.equiptableOption),
            $modal      :new ecss.tools.makeModal($container,'预警推送'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $type       :$container.find('[name=type]'),
            $alertsend:$container.find('#send'),
            $alertdelete:$container.find('#delete'),
            $window     :$(window)
        };
    };
    sendAlert=function(){
        var data=jqueryMap.$dtalert.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtalert.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteAlert=function(){
        var data=jqueryMap.$dtalert.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.objecttype}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下信息？',names,function(){
                modelMap.model.post('deletealert',function(){
                    jqueryMap.$dtalert.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            })
        }
    };
    setInfo=function(){
        modelMap.modeluser.post('get',function(data){
            console.log(data);
            jqueryMap.$form.getElementsByName('email').val(data.email);
            jqueryMap.$form.getElementsByName('phone').val(data.phone);
        },null,$(this).val(),'json');
    };
    addOptions=function(url,para,$select,text,async){
        modelMap.modeltools.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json',async);
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        var $user=jqueryMap.$form.getElementsByName('users');
        $user.html('<option value="">接收人</option>');
        addOptions('getuser',null,$user, '<option value="">接收人</option>',false);
        jqueryMap.$form.getElementsByName('manager').change(setInfo);
        jqueryMap.$type.click(changeTable);
        jqueryMap.$alertsend.click(sendAlert);
        jqueryMap.$alertdelete.click(deleteAlert);
    };
    return{initModule:initModule};
}());