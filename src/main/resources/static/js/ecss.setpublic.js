/**
 * Created by jason on 2015/1/30.
 */
ecss.setpublic=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">公示设置</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div class="col-lg-3" id="energytypelist"></div>'
            +'<div class="col-lg-3" id="organlist"></div>'
            +'<div class="col-lg-3" id="buildlist"></div>'
            +'<div class="col-lg-3" id="conditions"></div>'
            +'<div class="col-lg-3 "><div class="btn btn-success" id="update">设置生效</div></div>',
            condition:'<div id="condition"></div>',
            energytypelist:'<div id="energytypetree"></div>',
            organlist:'<div id="organtree"></div>',
            buildlist:'<div id="buildtree"></div>',
            energyTypeOption : {
                core:{
                    data:{
                        url:'getallenergyitems.do'
                    }
                },
                plugins:['checkbox','search']
            },
            organTreeOption : {
                core:{
                    data:{
                        url:'getallorgans.do'
                    }
                },
                plugins:['checkbox','search']
            },
            buildTreeOption : {
                core:{
                    data:{
                        url:'getbuildtree.do'
                    }
                },
                plugins:['checkbox','search']
            },
            formOption:[
                [{label:'label',text:'时间单位',size:3},{label:'input',type:'radio',name:'time',options:[{value:'year',text:'年',checked:true},{value:'month',text:'月'}],valid:{}}],
                [{label:'label',text:'时间',size:3},{label:'input',type:'date',name:'datetime',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'能耗级别',size:3},{label:'input',type:'text',name:'energylevel',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'建筑级别',size:3},{label:'input',type:'text',name:'buildlevel',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'部门级别',size:3},{label:'input',type:'text',name:'organlevel',size:8,valid:{notEmpty: {}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.public.setpublic)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap, initform, initModule,addEvent,update,setDatetime;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#energytypelist'),'能耗分项列表','orange','',configMap.energytypelist),
            $portlet1    :new ecss.tools.makePortlet($container.find('#organlist'),'机构列表','green','',configMap.organlist),
            $portlet2    :new ecss.tools.makePortlet($container.find('#conditions'),'公示条件','red','',configMap.condition),
            $portlet3    :new ecss.tools.makePortlet($container.find('#buildlist'),'建筑列表','blue','',configMap.buildlist),
            $energytypetree  :new ecss.tools.makeTree($container.find('#energytypetree'),configMap.energyTypeOption),
            $organtree  :new ecss.tools.makeTree($container.find('#organtree'),configMap.organTreeOption),
            $buildtree:new ecss.tools.makeTree($container.find('#buildtree'),configMap.buildTreeOption),
            $form       :new ecss.tools.makeForm($container.find('#condition'),configMap.formOption),
            $confirm    :new ecss.tools.makeModal($container),
            $update     :$container.find('#update'),
            $window     :$(window)
        };
    };
    initform=function(){
        var $typecode=jqueryMap.$form.getElementsByName('type');
        modelMap.model.post('getallorgantypes',function(data){
            console.log(data);
            if(data!=null){
                $.each(data.data,function(i,item){
                    $typecode.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,null,'json');
    };
    update=function(){
        console.log(jqueryMap.$buildtree.get_selected());
        var data= {
                setting:jqueryMap.$form.getElementsByName('time').val() + ',' + jqueryMap.$form.getElementsByName('datetime').val() + ',' + jqueryMap.$form.getElementsByName('energylevel').val() + ',' + jqueryMap.$form.getElementsByName('buildlevel').val() + ',' + jqueryMap.$form.getElementsByName('organlevel').val(),
                energytypes:jqueryMap.$energytypetree.get_selected().map(function (item) {return item.id+':'+(item.parents.length-1)}).join(','),
                builds:jqueryMap.$buildtree.get_selected().map(function (item) {return item.id+':'+(item.parents.length-1)}).join(','),
                organs:jqueryMap.$organtree.get_selected().map(function (item) {return item.id+':'+(item.parents.length-1)}).join(',')
            };
        modelMap.model.post('setpublic',function(){
            ZENG.msgbox.show('设置成功！',4,3000);
        },function(){
            ZENG.msgbox.show('设置失败！',5,3000);
        },data);
    };
    setDatetime=function(){
        jqueryMap.$form.getElementsByName('datetime').datepicker('remove');
        if($(this).val()=='year'){
            ecss.tools.makeDate(jqueryMap.$form.getElementsByName('datetime'),{format: 'yyyy',minViewMode:2,maxViewMode:2});
            jqueryMap.$form.getElementsByName('datetime').val((new Date()).getFullYear());
        }
        else{
            ecss.tools.makeDate(jqueryMap.$form.getElementsByName('datetime'),{format: 'yyyy/mm',minViewMode:1,maxViewMode:1});
            jqueryMap.$form.getElementsByName('datetime').val((new Date()).getFullYear()+"/"+((new Date()).getMonth() + 1).toString().pad(2));
        }
    };
    addEvent=function(){
        jqueryMap.$update.click(update);
        jqueryMap.$form.getElementsByName('time').click(setDatetime);
        ecss.tools.makeDate(jqueryMap.$form.getElementsByName('datetime'),{format: 'yyyy',minViewMode:2,maxViewMode:2});
        jqueryMap.$form.getElementsByName('datetime').val((new Date()).getFullYear());
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        initform();
    };
    return{initModule:initModule};
}());