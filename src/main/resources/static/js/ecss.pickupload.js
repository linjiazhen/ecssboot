/**
 * Created by jason on 2015/1/30.
 */
ecss.pickupload=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
            +'<div class="page-title">'
            +'<h4 class="pull-right strong" id="datetime"></h4>'
            +'<ol class="breadcrumb">'
            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
            +'<li class="active">数据上报</li>'
            +'</ol>'
            +'</div>'
            +'<div id="portlet"></div>'
            +'</div>',
            body:String()
            +'<div class="tablecontent">'
            +'<div class="buttons">'
            +'<button class="btn btn-success btn-sm" id="pickup">提取打包</button>'
            +'<button class="btn btn-warning btn-sm" id="upload">上报</button>'
            +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
            +'</div>'
            +'<table class="table table-hover table-bordered" id="DTreport"></table>'
            +'</div>',
            tableOption:{
                "ajax": { "url": "getallpickuploads.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"数据时间","data": "datatime",
                        render: function(data, type, full){
                            return moment(full.datatime).format('YYYY/MM/DD');
                        }},
                    { title:"提取状态","data": "getDatastatus",
                        render: function(data, type, full){
                            if(data==0) return '<text class="text-success">成功</text>';
                            else
                                return '<text class="text-danger">失败</text>';
                        }},
                    { title:"打包状态","data": "pickstatus",
                        render: function(data, type, full){
                            if(data==0) return '<text class="text-success">成功</text>';
                            else
                                return '<text class="text-danger">失败</text>';
                        }},
                    { title:"上报状态","data": "uploadstatus",
                        render: function(data, type, full){
                            if(data==0) return '<text class="text-success">成功</text>';
                            else
                                return '<text class="text-danger">失败</text>';
                        }},
                    { title:"操作时间","data": "operatetime" },
                    { title:"数据下载","data": 'filename' ,
                        render: function(data, type, full){
                            if(data!=null)
                                return '<a class="disclick" style="cursor: pointer" href="'+moment(full.datatime).format('YYYYMMDD')+'/zipfileDownload.do">'+data+'</a>';
                            else
                                return '无打包数据';
                        }
                    }
                ],
                "order": [[ 1, 'asc' ]]
            },
            formOption:[
                [{label:'label',text:'时间',size:3},{label:'input',type:'date',name:'date',size:8,valid:{notEmpty: {}}}]
            ]

        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.maintenance.pickupload),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  pickup,   upload, deleteList ,setInit;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'打包上传信息','green','',configMap.body),
            $dtpickload    :new ecss.tools.makeTable($container.find('#DTreport'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'数据时间选择'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $pickup   :$container.find('#pickup'),
            $upload:$container.find('#upload'),
            $delete:$container.find('#delete'),
            $window     :$(window)
        };
    };
    pickup=function(){
        jqueryMap.$modal.show(function () {
            modelMap.model.post('pickup',function(){
                jqueryMap.$dtpickload.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'打包失败！请刷新重试！');
            },jqueryMap.$form.save().date);
        });
    };
    upload=function(){
        jqueryMap.$modal.show(function () {
            modelMap.model.post('upload',function(){
                jqueryMap.$dtpickload.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'上传失败！请刷新重试！');
            },jqueryMap.$form.save().date);
        });
    };
    deleteList=function(){
        var data=jqueryMap.$dtpickload.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var dates=data.map(function(item){return moment(item.datatime).format('YYYY/MM/DD')}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下数据？',dates,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtpickload.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },dates);
            });
        }
    };
    setInit=function(){
        jqueryMap.$form.getElementsByName('date').datetimepicker({
            locale: 'zh-cn',
            format: 'YYYY/MM/DD'
        });
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$pickup.click(pickup);
        jqueryMap.$upload.click(upload);
        jqueryMap.$delete.click(deleteList);
        setInit();
    };
    return{initModule:initModule};
}());