/**
 * Created by jason on 2015/1/30.
 */
ecss.auditreport=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
            +'<div class="page-title">'
            +'<h4 class="pull-right strong" id="datetime"></h4>'
            +'<ol class="breadcrumb">'
            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
            +'<li class="active">审计报告</li>'
            +'</ol>'
            +'</div>'
            +'<div id="portlet"></div>'
            +'</div>',
            body:String()
            +'<div class="tablecontent">'
            +'<div class="buttons">'
            +'<button class="btn btn-success btn-sm" id="add">添加</button>'
            +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
            +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
            +'</div>'
            +'<table class="table table-hover table-bordered" id="DTreport"></table>'
            +'</div>',
            tableOption:{
                "ajax": { "url": "getallreports.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"审计对象","data": "buildname" },
                    { title:"审计时间","data": "date",
                        render: function(data, type, full){
                            return moment(data).format('YYYY/MM');
                        }},
                    { title:"审计人员","data": "author" },
                    { title:"报告下载","data": 'filename' ,
                        render: function(data, type, full){
                            if(data!=null)
                                return '<a class="disclick" style="cursor: pointer" href="'+full.uuid+'/fileDownload.do">'+data+'</a>';
                            else
                                return '<a class="disclick" style="cursor: pointer" href="template/fileDownload.do">审计报告模版.doc</a>'
                        }
                    },
                    { title:"上传文件","data": 'uuid' ,className:'disclick',
                        render: function(data, type, full){
                            return '<span class="btn btn-success fileinput-button disclick" style="margin: 0;padding: 2px 10px;">'
                                +'<i class="glyphicon glyphicon-plus"></i>'
                                +'<span>文件上传</span>'
                                +'<input class="fileupload" id="'+data+'" type="file" name="files[]" multiple>'
                                +' </span>'
                        }
                    }
                ],
                "order": [[ 3, 'asc' ]],
                "initComplete":function(){
                    $('.fileupload').fileupload({
                        url: 'fileUploader.do',
                        formData:function(){
                            return [{name:'uuid',value:this.fileInput[0].id}];
                        }
                    }).bind('fileuploadprogress', function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        ZENG.msgbox.show('已上传'+progress + '%',1,0);
                    }).bind('fileuploaddone', function (e, data) {
                        ZENG.msgbox.show('上传成功！',4,3000);
                        jqueryMap.$dtreport.reload();
                    }).bind('fileuploadfail', function (e, data) {
                        ZENG.msgbox.show('上传失败！',5,3000);
                    });
                }
            },
            formOption:[
                [{label:'label',text:'区域',size:3},{label:'select',name:'group',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'楼栋',size:3},{label:'select',name:'build',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'审计时间',size:3},{label:'input',type:'date',name:'date',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'审计人员',size:3},{label:'input',type:'text',name:'author',size:8,valid:{notEmpty: {}}}]
            ]

        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.audit.audit),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addReport,   updateReport, deleteReport,setInit,addOptions;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'审计报告','green','',configMap.body),
            $dtreport    :new ecss.tools.makeTable($container.find('#DTreport'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'审计报告信息'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $reportadd   :$container.find('#add'),
            $reportupdate:$container.find('#update'),
            $reportdelete:$container.find('#delete'),
            $window     :$(window)
        };
    };
    addReport=function(){
        jqueryMap.$form.add();
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('add',function(){
                jqueryMap.$dtreport.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateReport=function(){
        var data=jqueryMap.$dtreport.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtreport.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteReport=function(){
        var data=jqueryMap.$dtreport.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.build}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下区域？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtreport.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    addOptions=function(url,para,$select,text){
        modelMap.modeltools.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json');
    };
    setInit=function(){
        var $group=jqueryMap.$form.getElementsByName('group');
        $group.html('<option value="">选择区域</option>');
        addOptions('getgroup',null,$group, '<option value="">选择区域</option>');
        var $build=jqueryMap.$form.getElementsByName( 'build');
        $build.html('<option value="">选择楼栋</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option value="">选择楼栋</option>');
        });
        new ecss.tools.makeDate(jqueryMap.$form.getElementsByName('date'),{
            autoclose:true,
            language: 'zh-CN',
            format: 'yyyy/mm',
            weekStart:1,
            endDate:'0d',
            clearBtn:true,
            minViewMode:1});
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$reportadd.click(addReport);
        jqueryMap.$reportupdate.click(updateReport);
        jqueryMap.$reportdelete.click(deleteReport);
        setInit();
    };
    return{initModule:initModule};
}());