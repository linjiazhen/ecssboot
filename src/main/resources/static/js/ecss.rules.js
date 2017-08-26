/**
 * Created by jason on 2015/1/30.
 */
ecss.rules=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">规则管理</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div id="portlet" class="col-lg-12"></div>',
            body:String()
                +'<div class="tablecontent">'
                    +'<div class="buttons">'
                        +'<button class="btn btn-success btn-sm" id="add">添加</button>'
                        +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                        +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                        +'<button class="btn btn-info btn-sm" id="setall">预警设置</button>'
                    +'</div>'
                    +'<table class="table table-hover table-bordered" id="DTrule"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getallrules.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"机构","data": "objecttype" ,"render":function ( data , type, full) {
                            return (full.first?full.first:'')+(full.second?('-'+full.second):'')+(full.third?('-'+full.third):'');
                    }},
                    { title:"监控级别","data": "objecttype" ,"render":function ( data , type, full) {
                            if(full.organlevel=='first') return '一级';
                            else
                            if(full.organlevel=='second') return '二级';
                            else
                            if(full.organlevel=='third') return '三级';
                    }},
                    { title:"能耗分项","data": "energyitem" },
                    { title:"时间单位","data": "timeunit" ,"render":function ( data , type, full){
                        if(data=='year') return '年';
                        else
                        if(data=='month') return '月';
                        else
                        if(data=='day') return '日';
                    }},
                    { title:"额定值","data": "value" },
                    { title:"创建时间","data": "time" },
                    { title:"使用","data": "state",visible:false},
                    { title:"使用","data": "state",
                        "render":function ( data, type ) {
                            if(data=='0')
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" ></label> </div></div>';
                            else if(data=='1')
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div>';
                        }}
                ],
                "order": [[ 7, 'desc' ],[ 8, 'asc' ]]
            },
            formAllOption:[
                [{label:'label',text:'自动预警',size:3},{label:'input',type:'radio',name:'isalert',options:[{value:'1',text:'开'},{value:'0',text:'关'}],size:8,valid:{}}],
                [{label:'label',text:'预警方式',size:3},{label:'input',type:'checkbox',name:'alerttype',options:[{value:'1',text:'邮箱'},{value:'2',text:'短信'}],size:8,valid:{}}],
                [{label:'label',text:'预警比例',size:3},{label:'input',type:'text',name:'percent',size:8,valid:{}}]
            ],
            formEquipOption:[
                [{label:'label',text:'表类型',size:3},{label:'input',type:'radio',name:'equiptype',options:[{value:'1',text:'电表',checked:true},{value:'0',text:'水表'}],size:8,valid:{}}],
                [{label:'label',text:'市政表',size:3},{label:'input',type:'text',name:'amcivicism',size:8,valid:{}}],
                [{label:'label',text:'校级表',size:3},{label:'input',type:'text',name:'amschool',size:8,valid:{}}],
                [{label:'label',text:'区域表',size:3},{label:'input',type:'text',name:'amgroup',size:8,valid:{}}],
                [{label:'label',text:'楼宇表',size:3},{label:'input',type:'text',name:'ambuild',size:8,valid:{}}],
                [{label:'label',text:'楼层表',size:3},{label:'input',type:'text',name:'amfloor',size:8,valid:{}}],
                [{label:'label',text:'房间表',size:3},{label:'input',type:'text',name:'amroom',size:8,valid:{}}],
                [{label:'label',text:'市政表',size:3},{label:'input',type:'text',name:'wmcivicism',size:8,valid:{}}],
                [{label:'label',text:'校级表',size:3},{label:'input',type:'text',name:'wmschool',size:8,valid:{}}],
                [{label:'label',text:'区域表',size:3},{label:'input',type:'text',name:'wmgroup',size:8,valid:{}}],
                [{label:'label',text:'楼宇表',size:3},{label:'input',type:'text',name:'wmbuild',size:8,valid:{}}],
                [{label:'label',text:'楼层表',size:3},{label:'input',type:'text',name:'wmfloor',size:8,valid:{}}],
                [{label:'label',text:'房间表',size:3},{label:'input',type:'text',name:'wmroom',size:8,valid:{}}]
            ],
            formOption:[
                [{label:'label',text:'一级机构',size:3},{label:'select',name:'first',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'二级机构',size:3},{label:'select',name:'second',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'三级机构',size:3},{label:'select',name:'third',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'监控级别',size:3},{label:'input',type:'radio',name:'organlevel',options:[{value:'first',text:'一级',checked:true},{value:'second',text:'二级'},{value:'third',text:'三级'}],size:8,valid:{}}],
                [{label:'label',text:'能耗分项',size:3},{label:'select',name:'energyitem',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'时间单位',size:3},{label:'input',type:'radio',name:'timeunit',options:[{value:'year',text:'年',checked:true},{value:'month',text:'月'},{value:'day',text:'日'}],size:8,valid:{}}],
                [{label:'label',text:'额定值',size:3},{label:'input',type:'text',name:'value',size:8,valid:{notEmpty: {}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.alert.rule),
            modeluser:new ecss.tools.makeModel(ecss.model.user.user),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  init,addRule,  setOnOff, updateRule, deleteRule,getData,setAim,setInfo,setEquip,changeEquip,setAll,addOptions;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'规则库','green','',configMap.body),
            $modalall      :new ecss.tools.makeModal($container,'全局设置'),
            $formall       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formAllOption),
            $modalequip    :new ecss.tools.makeModal($container,'异常差值设置'),
            $formequip      :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formEquipOption),
            $dtrule    :new ecss.tools.makeTable($container.find('#DTrule'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'规则设置'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $ruleadd   :$container.find('#add'),
            $ruleupdate   :$container.find('#update'),
            $ruledelete:$container.find('#delete'),
            $setall:     $container.find('#setall'),
            $setequip:   $container.find('#setequip'),
            $window     :$(window)
        };
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
    init=function(){
        var $first=jqueryMap.$form.getElementsByName('first');
        $first.html('<option value="j4_2">全校</option>');
        addOptions('getorgan','j4_2',$first, '<option value="">全校</option>');
        var $second=jqueryMap.$form.getElementsByName('second');
        $second.html('<option value="#">全机构</option>');
        $first.change(function(){
            addOptions('getorgan',$first.val(),$second,'<option value="#">全机构</option>');
        });
        var $third=jqueryMap.$form.getElementsByName('third');
        $third.html('<option value="#">全部门</option>');
        $second.change(function(){
            addOptions('getorgan',$second.val(),$third,'<option value="#">全部门</option>');
        });
        var $energytype=jqueryMap.$form.getElementsByName('energyitem');
        modelMap.modeltools.post('getenergyitems',function(data){
            if(data!=null){
                $.each(data,function(i,item){
                    $energytype.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,null,'json');
    };
    addRule=function(){
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('add',function(){
                jqueryMap.$dtrule.reload();
                jqueryMap.$modal.hide();
            },function(){
                ZENG.msgbox.show('添加失败！请刷新重试！',5,3000);
            });
        });
    };
    updateRule=function(){
        var data=jqueryMap.$dtrule.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            if(data[0].firstid) {
                jqueryMap.$form.getElementsByName('first').val(data[0].firstid?data[0].firstid:'#');
                addOptions('getorgan', data[0].firstid, jqueryMap.$form.getElementsByName('second'), '<option value="#">全机构</option>', false);
            }
            if(data[0].secondid) {
                jqueryMap.$form.getElementsByName('second').val(data[0].secondid?data[0].secondid:'#');
                addOptions('getorgan', data[0].secondid, jqueryMap.$form.getElementsByName('third'), '<option value="#">全部门</option>', false);
            }
            if(data[0].thirdid) {
                jqueryMap.$form.getElementsByName('third').val(data[0].thirdid?data[0].thirdid:'#');
            }
            jqueryMap.$form.getElementsByName('energyitem').val(data[0].energyitemcode);
            console.log(data[0]);
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtrule.reload();
                    jqueryMap.$modal.hide();
                    ZENG.msgbox.show('修改成功！',4,3000);
                },function(){
                    ZENG.msgbox.show('修改失败！',5,3000);
                });
            });
        }
    };
    deleteRule=function(){
        var data=jqueryMap.$dtrule.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){
                return (item.first?item.first:'')+(item.second?('-'+item.second):'')+(item.third?('-'+item.third):'');
            }).join(",");
            jqueryMap.$confirm.confirm('确认删除以下区域？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtrule.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ZENG.msgbox.show('删除失败！请刷新重试！',5,3000);
                },ids);
            })
        }
    };
    setOnOff=function(){
        var check=$(this).prop('checked');
        var togglebutton=$(this);
        togglebutton.closest('tr').addClass('success');
        var data=jqueryMap.$dtrule.getSelect();
        var ids=data.map(function(item){return item.uuid}).join(":");
        var names=data.map(function(item){
            return (item.first?item.first:'')+(item.second?('-'+item.second):'')+(item.third?('-'+item.third):'');
        }).join(",");
        jqueryMap.$confirm.confirm('确认'+check?'开启':'关闭'+data.length+'条规则？',names,function(){
            modelMap.model.post('setruleonoff',function(){
                togglebutton.closest('tbody').find('.success input[name=use]').prop('checked',check);
                jqueryMap.$confirm.hide();
                jqueryMap.$dtrule.reload();
            },function(){
                ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'设置失败！请刷新重试！');
            },ids+'@'+(check?'1':'0'));
        },function(){
            togglebutton.prop('checked',!check);
        });
    };
    setAll=function(){
        jqueryMap.$modalall.show(function () {
            var data=jqueryMap.$formall.save();
            var types=data.alerttype.split(',');
            if(types.length==2) data.alerttype=parseInt(types[0])+parseInt(types[1]);
            modelMap.model.post('savealertconf',function(){
                jqueryMap.$modalall.hide();
                ZENG.msgbox.show('保存成功！',4,3000);
            },function(){
                ZENG.msgbox.show('保存失败！',5,3000);
            },data);
        });
    };
    changeEquip=function(){
        if($(this).val()==1){
            jqueryMap.$container.find('[name^=wm]').closest('.form-group').hide();
            jqueryMap.$container.find('[name^=am]').closest('.form-group').show();
        }
        else{
            jqueryMap.$container.find('[name^=am]').closest('.form-group').hide();
            jqueryMap.$container.find('[name^=wm]').closest('.form-group').show();
        }
    };
    setEquip=function(){
        jqueryMap.$modalequip.show(function () {
            modelMap.model.post('savealertequip', function () {
                jqueryMap.$modalequip.hide();
                ZENG.msgbox.show('保存成功！', 4, 3000);
            }, function () {
                ZENG.msgbox.show('保存失败！', 5, 3000);
            }, jqueryMap.$formequip.save());
        });
    };
    getData=function(){
        modelMap.model.post('getalertconf',function(data){
            data.alerttype=data.alerttype.toString();
            if(data.alerttype=='3') data.alerttype='1,2';
            jqueryMap.$formall.update(data);
        },null,null,'json');
        modelMap.model.post('getalertequip',function(data){
            jqueryMap.$formequip.update(data);
        },null,null,'json');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        init();
        stateMap.$container.find('[name^=wm]').closest('.form-group').hide();
        jqueryMap.$formequip.getElementsByName('equiptype').change(changeEquip);
        jqueryMap.$container.find('#DTrule tbody').on('change',' input[name=use]',setOnOff);
        jqueryMap.$ruleadd.click(addRule);
        jqueryMap.$ruleupdate.click(updateRule);
        jqueryMap.$ruledelete.click(deleteRule);
        jqueryMap.$setall.click(setAll);
        jqueryMap.$setequip.click(setEquip);
        getData();
    };
    return{initModule:initModule};
}());