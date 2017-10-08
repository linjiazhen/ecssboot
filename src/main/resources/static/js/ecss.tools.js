/**
 * Created by jason on 2015/1/30.
 */
ecss.tools=(function(){
    'use strict';
    var makeModel,  makeAtm,
        makeTable,  makeTree,
        makeChart,  makeForm,
        popover,    makeModal,
        makeCircleTile,
        makePortlet,
        makeDateRange,makeDate;
    Array.prototype.unique = function(){//数组去重函数
        var res = [];
        var json = {};
        for(var i = 0; i < this.length; i++){
            if(!json[this[i]]){
                res.push(this[i]);
                json[this[i]] = 1;
            }
        }
        return res;
    };
    makeModel=function(model) {
        var modelurl=model.url;
        var modeldata=model.data;

        this.post = function (operate, success, error, data,datatype, async) {
            var defaultSuc=function(data){
                modeldata=data;
            };
            var defaultFail=function(data){
                console.log(data);
            };
            var option = {
                type: 'post',
                url: modelurl[operate],
                data: modeldata,
                dataType: 'text',
                async: true,
                success: defaultSuc,
                error: defaultFail
            };
            if(data!=null)
                option.data=data;
            if (async != null)
                option.async = async;
            if (datatype != null)
                option.dataType = datatype;
            if (success!=null)
                option.success = success;
            if (error!=null)
                option.error = error;
            if (typeof data == 'string')
                option.contentType='text/plain';
            console.log(option.url);
            console.log(option.data);
            $.ajax(option);
        };
        this.get = function(){
            return modeldata;
        };
        this.set =function(data){
            if(data.disabled!=null){
                $.each(data.disabled,function(i,item){
                    delete modeldata[item];
                });
            }
            delete data.disabled;
            $.each(data,function(key,value){
                modeldata[key]=value;
            });
        };
        this.setprop=function(key,value){
            modeldata[key]=value;
        };
    };
    makeTable=function($table,option) {
        $.fn.dataTable.ext.errMode = 'none';
        var opt=this.option = {
            "language":{
                "sProcessing":   "处理中...",
                "sLengthMenu":   "显示 _MENU_ 项结果",
                "sZeroRecords":  "没有匹配结果",
                "sInfo":         "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty":    "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix":  "",
                "sSearch":       "搜索:",
                "sUrl":          "",
                "sEmptyTable":     "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands":  ",",
                "oPaginate": {
                    "sFirst":    "首页",
                    "sPrevious": "上页",
                    "sNext":     "下页",
                    "sLast":     "末页"
                },
                "oAria": {
                    "sSortAscending":  ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "dom": 'frt<"col-sm-5"l><"col-sm-3"i>p',
            "order": [[1, 'asc']],
            "multiple":true
        };
        if(option!=null)
            $.each(option,function(index,data){
                opt[index]=data;
            });
        if(opt.buttons) opt.dom='B'+opt.dom;
        var table=this.table=$table.DataTable(this.option);

        table.buttons().container().appendTo( $('.tablecontent .buttons') );

        this.table.on( 'draw.dt', function () {
            $.material.init();
        } );
        this.table.on('page.dt', function () {
            table.rows('.success').nodes().to$().removeClass('success');
           // console.log(table.rows('.success').nodes().to$());
        });
        $table.find('#chooseall').click(function(){
            if($(this).is(":checked")){
                $table.find("tbody tr").addClass("success");
                $table.find("input.choose").prop('checked',true);
            }
            else{
                $table.find("input.choose").prop('checked',false);
                $table.find("tbody tr").removeClass("success");
            }
        });
        if(opt.multiple)
            $table.find('tbody').on('click','td:not(:has(.disclick))',function(event){
                if($(this).parent("tr").hasClass('success')) {
                    $(this).parent("tr").removeClass("success");
                    $(this).parent("tr").find('.choose').prop('checked',false);
                }
                else {
                    $(this).parent("tr").addClass("success");
                    $(this).parent("tr").find('.choose').prop('checked',true);
                }
                event.preventDefault();
            });
        else
            $table.find('tbody').on('click','td:not(:has(.disclick))',function(event){
                $table.find('tbody tr').removeClass("success");
                $table.find('tbody tr').find('.choose').prop('checked',false);
                $(this).parent("tr").addClass("success");
                $(this).parent("tr").find('.choose').prop('checked',true);
                event.preventDefault();
            });
        this.setFixedCol=function(leftCol,rightCol){
            new $.fn.dataTable.FixedColumns( this.table, {
                leftColumns: leftCol,
                rightColumns: rightCol
            } );
            $(".DTFC_LeftFootWrapper").css("top","10px");
            $(".DTFC_RightFootWrapper").css("top","10px");
        };
        this.setShowNumber=function(){
            this.table.on( 'order.dt search.dt', function () {
                table.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                    cell.innerHTML = i+1;
                } );
            } ).draw();
        };
        this.getSelect=function(){
            return this.table.rows('.success').data();
        };
        this.getDataByClass=function(classname){
            return this.table.rows('.'+classname).data();
        };
        this.getChildShown=function(){
            return this.table.row('.shown').data();
        };
        this.getData=function(select){
            return this.table.rows(select).data();
        };
        this.removeSelect=function(){
            this.table.rows('.success').remove().draw();
        };
        this.setData=function(data,clear){
            if(clear)
                this.table.clear();
            this.table.rows.add(data).draw();
        };
        this.setRowData=function(data,clear){
            if(clear)
                this.table.clear();
            this.table.row.add(data).draw();
        };
        this.reload=function(){
            this.table.ajax.reload( null, false );
        };
        this.hide=function(){
            $(this.table.table().container()).hide();
        };
        this.show=function(){
            $(this.table.table().container()).show();
        };
        this.clear=function(){
            this.table.clear().draw();
        };
        this.setChild=function(childFormat,openfunc,closefunc){
            $table.on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table.row( tr );
                if ( row.child.isShown() ) {
                    row.child.hide();
                    tr.removeClass('shown');
                    if(closefunc!=null)
                        closefunc();
                }
                else {
                    $.each(table.rows('.shown'),function(i,item){
                        table.row(item).child.hide();
                        table.row(item).nodes().toJQuery().removeClass('shown');
                    });
                    row.child( childFormat(row.data()) ).show();
                    tr.addClass('shown');
                    if(openfunc!=null)
                        openfunc(row.data());
                }
            } );
        };
        this.setDblClick=function(func){
            $table.on('dblclick','tbody tr',function(){
                func(table.row($(this)).data());
            });
        };
        this.destroy=function(){
            this.table.destroy();
        };
    };
    makeTree=function($container,option,height){
        var treeString=
            '<div class="input-group">'
            +'<input id="search" type="text" class="form-control" placeholder="搜索">'
            +'<span class="input-group-addon"><i class="mdi-action-search"></i></span>'
            +'</div>'
            +'<div class="tree treebox"></div>';
        $container.append(treeString);
        if(height){
            $container.find('.treebox').css('height',height);
        }
        var $tree=$container.find('.tree');
        var to=false;
        $container.find('#search').keyup(function () {
            if(to) { clearTimeout(to); }
            to = setTimeout(function () {
                var v = $container.find('#search').val();
                $tree.jstree(true).search(v);
            }, 250);
        });
        var opt=this.option={
            plugins:['search']
        };
        var allnode=null,topnode=null;
        if(option!=null)
            $.each(option,function(index,data){
                opt[index]=data;
            });
        $tree.jstree(this.option);
        $tree.on('ready.jstree', function (){
            $tree.jstree(true).load_all();
        });
        $tree.on('load_all.jstree', function (e,data) {
            allnode=data.node.children_d;
            topnode=data.node.children;
            $tree.jstree(true).open_node(topnode);
        });
        this.setInitEvent=function(func){
            $tree.on('load_all.jstree',func);
        };
        this.setSelectEvent=function(func){
            $tree.on('select_node.jstree',func);
        };
        this.setCheckEvent=function(func){
            $tree.on('check_node.jstree',func);
        };
        this.setUncheckEvent=function(func){
            $tree.on('uncheck_node.jstree',func);
        };
        this.open_node=function(node){
            if(node==null)
                $tree.jstree(true).open_node(topnode);
            else
                $tree.jstree(true).open_all(node);
        };
        this.close_all=function(){
            $tree.jstree(true).close_all();
            $tree.jstree(true).open_node(topnode);
        };
        this.disable_node=function(node){
            if(node==null)
                $tree.jstree(true).disable_node(allnode);
            else
                $tree.jstree(true).disable_node(node);
        };
        this.enable_node=function(node){
            if(node==null)
                $tree.jstree(true).enable_node(allnode);
            else
                $tree.jstree(true).enable_node(node);
        };
        this.get_checked=function(pos){
            if(pos=='top')
                return $tree.jstree(true).get_top_checked(true);
            else
            if(pos=='bottom')
                return $tree.jstree(true).get_bottom_checked(true);
            else
                return $tree.jstree(true).get_checked(true);
        };
        this.get_selected=function(){
            return $tree.jstree(true).get_selected(true);
        };
        this.get_node=function(node){
            return $tree.jstree(true).get_node(node);
        };
        this.rename_node=function(node,val){
            console.log($tree.jstree(true).get_text(node));
            console.log($tree.jstree(true).rename_node(node,val));
            console.log($tree.jstree(true).get_text(node));
        };
        this.get_path=function(node){
            return $tree.jstree(true).get_path(node,null,true);
        };
        this.select_node=function(node){
            if(node==null)
                $tree.jstree(true).select_node(allnode);
            else
                $tree.jstree(true).select_node(node);
        };
        this.deselect_node=function(node){
            if(node==null)
                $tree.jstree(true).deselect_node(allnode);
            else
                $tree.jstree(true).deselect_node(node);
        };
        this.create_node=function(par,newnode,func){
            $tree.one('create_node.jstree',func);
            $tree.jstree(true).create_node(par,newnode);
            $tree.jstree(true).open_node(par);
        };
        this.check_node=function(node){
            if(node==null)
                $tree.jstree(true).check_all();
            else
                $tree.jstree(true).check_node(node);
        };
        this.check_topnode=function(){
            $tree.jstree(true).check_node(topnode);
        };
        this.check_childnode=function(node){
            $tree.jstree(true).check_node($tree.jstree(true).get_node(node).children);
        };
        this.uncheck_node=function(node){
            if(node==null)
                $tree.jstree(true).uncheck_all();
            else
                $tree.jstree(true).uncheck_node(node);
        };
        this.delete_node=function(node){
            $tree.jstree(true).delete_node(node);
        };
        this.refresh=function(){
            $tree.jstree(true).refresh();
        };
    };
    makeChart=function(chartid,option){
        var opts=this.option={
            /*            toolbox: {
             show : true,
             orient: 'horizontal',      // 布局方式，默认为水平布局，可选为：'horizontal' ¦ 'vertical'
             x: 'left',                // 水平安放位置，默认为全图右对齐，可选为： 'center' ¦ 'left' ¦ 'right'¦ {number}（x坐标，单位px）
             y: 'top',                  // 垂直安放位置，默认为全图顶端，可选为： 'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
             color : ['#1e90ff','#22bb22','#4b0082','#d2691e'],
             backgroundColor: 'rgba(0,0,0,0)', // 工具箱背景颜色
             borderColor: '#ccc',       // 工具箱边框颜色
             borderWidth: 0,            // 工具箱边框线宽，单位px，默认为0（无边框）
             padding: 5,                // 工具箱内边距，单位px，默认各方向内边距为5，
             showTitle: true,
             feature : {
             dataZoom : {show : true, title : {dataZoom : '区域缩放', dataZoomReset : '区域缩放-后退'}},
             dataView : {show : true, title : '数据视图', readOnly:true, lang : ['数据视图', '关闭', '刷新']},
             magicType: {show : true, title : {line : '折线图', bar : '柱形图', stack : '堆积', tiled : '平铺'}, type : ['line', 'bar', 'stack', 'tiled']},
             restore : {show : true, title : '还原', color : 'black'},
             saveAsImage : {show : true, title : '保存为图片', type : 'jpeg', lang : ['点击本地保存']}
             }
             }
             tooltip:{
             trigger:'axis'
             },
             legend: {
             data:[]
             },
             calculable : true,
             xAxis: [],
             yAxis:{
             min:0
             },
             dataZoom : {show : true, realtime : true, start : 0, end : 100},
             series:[]*/
        };
        this.mergeOpt=function(opt){
            if(opt!=null)
                $.each(opt,function(index,data){
                    opts[index]=data;
                });
        };
        this.mergeOpt(option);
        var chart=echarts.init(document.getElementById(chartid));
        chart.setOption(this.option);
        this.clear=function(){
            chart.clear();
        };
        this.setData=function(option){
            chart.clear();
            chart.hideLoading();
            this.mergeOpt(option);
            chart.setOption(this.option,true);
        };
        this.noData=function(){
            chart.clear();
            chart.showLoading({
                text:'无数据显示',
                effect:'bubble'
            });
        };
        this.setClickEvent=function(func){
            chart.on(echarts.config.EVENT.CLICK,func);
        };
        this.setDblClickEvent=function(func){
            chart.on(echarts.config.EVENT.DBLCLICK,func);
        };
    };
    /*
     * modal:type is modal,title is string,option is formoption,model is model data;
     * confirm:type is confirm,title is confirm content,option is function,model is null;
     * */
    //makeDateRange=function($div,option,func){
    //    var opt={
    //        locale: {
    //            applyLabel: '确定',
    //                cancelLabel: '取消',
    //                fromLabel: '从',
    //                toLabel: '到',
    //                customRangeLabel: '选择日期',
    //                daysOfWeek: ['日', '一', '二', '三', '四', '五','六'],
    //                monthNames: ['一月', '二月', '三月', '四月', '五月',
    //                '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    //                firstDay: 1
    //        },
    //        ranges: {
    //            '今天': [new Date(), new Date()],
    //            '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
    //            '最近7天': [moment().subtract('days', 6), new Date()],
    //            '最近30天': [moment().subtract('days', 29), new Date()],
    //            '本月': [moment().startOf('month'), moment().endOf('month')],
    //            '上个月': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
    //        },
    //        format: 'YYYY/MM/DD',
    //        showDropdowns:true,
    //        opens:'left',
    //        singleDatePicker:true
    //    };
    //    if(option!=null)
    //        $.each(option,function(index,data){
    //            opt[index]=data;
    //        });
    //    $div.daterangepicker(opt,func);
    //};
    makeDate=function($div,option){
        var $contain=$div;
        var html=$div.html();
        var opt={
            autoclose:true,
            language: 'zh-CN',
            format: 'yyyy/mm/dd',
            //todayBtn:'linked',
            //todayHighlight:true,
            weekStart:1,
            endDate:'0d',
            clearBtn:true,
            minViewMode:0,
            isRange:false
        };
        if(option!=null)
            $.each(option,function(index,data){
                opt[index]=data;
            });
        if(opt.isRange) {
            $contain.find('[name=start]').datepicker(opt);
            $contain.find('[name=end]').datepicker(opt);
        }
        else
            $contain.datepicker(opt);
        this.update=function(option,func){
            if(option!=null)
                $.each(option,function(index,data){
                    opt[index]=data;
                });
            if(opt.isRange) {
                $contain.find('[name=start]').datepicker('remove');
                $contain.find('[name=end]').datepicker('remove');
                $contain.html(html);
                $contain.find('[name=start]').datepicker(opt).on('changeDate', func);
                $contain.find('[name=end]').datepicker(opt).on('changeDate', func);
            }
            else{
                $contain.datepicker('remove');
                $contain.html(html);
                $contain.datepicker(opt).on('changeDate', func);
            }
        };
    };
    makeForm= function ($form,option) {
        $form.append('<form class="form-horizontal"></form>');
        var createForm=function($formBody,option) {
            var validOption= {
                locale: 'zh_CN',
                framework: 'bootstrap',
                excluded: ':disabled',
                icon: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {}
            };
            $.each(option, function (index, row) {
                var $formGroup = $formBody.append('<div class="form-group"></div>').find('.form-group:last');
                $.each(row, function (index, data) {
                    //QAQ:
                    var sizeClass=String();
                    if(typeof data.size != "object"){
                        sizeClass='col-xs-'+data.size;
                    }
                    else{
                        if(data.size.length>=1){
                            if(data.size[0]==0) sizeClass='hidden-xs';
                            else sizeClass='col-xs-'+data.size[0];
                        }
                        if(data.size.length>=2) {
                            if(data.size[1]==0) sizeClass=sizeClass+' hidden-sm';
                            else sizeClass=sizeClass+' col-sm-'+data.size[1];
                        }
                        if(data.size.length>=3){
                            if(data.size[2]==0) sizeClass=sizeClass+' hidden-md';
                            else sizeClass=sizeClass+' col-md-'+data.size[2];
                        }
                        if(data.size.length>=4){
                            if(data.size[3]==0) sizeClass=sizeClass+' hidden-lg';
                            else sizeClass=sizeClass+' col-lg-'+data.size[3];
                        }
                    }
                    //QAQ:
                    switch (data.label) {
                        case 'label':
                        {
                            $formGroup.append('<label class="'+sizeClass+ ' control-label">' + data.text + '</label>').find('label:last').addClass(data.class);
                            break;
                        }
                        case 'input':
                        {
                            if(data.type=='radio'){
                                var $radio=$formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '</div>').find('div').last();
                                $.each(data.options,function(index,item){
                                    $radio.append('<div class="radio radio-primary radio-inline">'
                                        +'<label>'
                                        +'<input type="radio" name="'+data.name+'"  value="'+item.value+'"/>'+item.text
                                        +'</label>'
                                        +'</div>'
                                    ).find('input:last').addClass(data.class);
                                    if(item.checked) $radio.find('input:last').prop('checked',true);
                                });
                                validOption.fields[data.name] = {validators: data.valid};
                            }
                            else
                            if(data.type=='checkbox'){
                                var $checkbox=$formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '</div>').find('div').last();
                                $.each(data.options,function(index,item){
                                    $checkbox.append('<div class="checkbox checkbox-primary checkbox-inline" style="margin-top: -20px">'
                                        +'<label>'
                                        +'<input type="checkbox" name="'+data.name+'"  value="'+item.value+'" />'+item.text
                                        +'</label>'
                                        +'</div>'
                                    ).find('input:last').addClass(data.class);
                                    if(item.checked) $checkbox.find('input:last').prop('checked',true);
                                });
                                validOption.fields[data.name] = {validators: data.valid};
                            }
                            else
                            if(data.type=='daterange'){
                                $formGroup.append(''
                                    + '<div class="input-group '+sizeClass+ '" id="datepicker">'
                                    + '<div class="col-lg-12"><input type="text" class="form-control" name="start" placeholder="开始时间"/></div>'
                                    + '<span class="input-group-addon">到</span>'
                                    + '<div class="col-lg-12"><input type="text" class="form-control" name="end" placeholder="结束时间"/></div>'
                                    + '</div>');
                                //        validOption.fields[data.name] = {validators: data.valid};
                                //         makeDateRange($formGroup.find('input:last'),data.option);
                            }
                            else
                            if(data.type=='date'){
                                $formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '<input  name="' + data.name + '" class="form-control"/>'
                                    + '</div>').find('input:last').addClass(data.class);
                                validOption.fields[data.name] = {validators: data.valid};
                                //         makeDate($formGroup.find('input:last'),data.option);
                            }
                            else{
                                $formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '<input type="'+data.type+'" name="' + data.name + '" class="form-control"/>'
                                    + '</div>').find('input:last').addClass(data.class);
                                validOption.fields[data.name] = {validators: data.valid};
                            }
                            break;
                        }
                        case 'textarea':
                        {
                            if(data.type=='tag') {
                                $formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '<textarea id="tag" rows="' + data.row + '" name="' + data.name + '" class="form-control"></textarea>'
                                    + '</div>').find('#tag').last().tagEditor();
                            }
                            else{
                                $formGroup.append(''
                                    + '<div class="'+sizeClass+ '">'
                                    + '<textarea  rows="' + data.row + '" name="' + data.name + '" class="form-control"></textarea>'
                                    + '</div>');
                                validOption.fields[data.name] = {validators: data.valid};
                            }
                            $formGroup.find('textarea:last').addClass(data.class);
                            break;
                        }
                        case 'select':
                        {
                            var $select=$formGroup.append(''
                                + '<div class="'+sizeClass+ '">'
                                + '<select name=' + data.name + ' class="form-control"'+(data.multiple?'multiple="multiple"':'')+(data.row?' size='+data.row:'')+'></select>'
                                + '</div>').find('select:last').addClass(data.class);
                            if (typeof data.options == 'object') {
                                $.each(data.options,function(index,item){
                                    $select.append('<option value='+item.value+'>'+item.text+'</option>');
                                });
                            }
                            validOption.fields[data.name] = {validators: data.valid};
                            break;
                        }
                        case 'table':
                        {
                            var $buttons=$formGroup.append(''
                                + '<div class="'+sizeClass+ '">'
                                +'<div class="buttons">'
                                +'</div>'
                                +'<table class="table table-hover table-bordered" id="'+data.id+'">'
                                +'</div>').find('.buttons:last');
                            $.each(data.buttons,function(index,item){
                                $buttons.append('<div class="btn btn-sm" >'+item.text+'</div>').find('.btn:last').addClass(item.class);
                                $.each(item.funcs,function(index,item){
                                    $buttons.find('div:last').bind(item.event,item.func);
                                });
                            });
                            break;
                        }
                        case 'image':
                        {
                            $formGroup.append('<img src="'+data.src+'" class="'+data.class+'">');
                        }
                    }
                    if(data.disable!=null) $formGroup.find('[name='+data.name+']').prop('disabled',data.disable);
                    if(data.adds!=null){
                        if(data.adds=='checkbox'){
                            var $div=$formGroup.find('[name='+data.name+']').wrap('<div class="input-group"></div>');
                            $div.addClass('check');
                            $div.after("<span class='input-group-addon' ><div class='checkbox' style='margin-top: -25px'><label><input type='checkbox'></label></div></span>").next().find('input').click(function(){
                                var value=$(this).prop('checked');
                                if(value) $div.prop('disabled',false);
                                else $div.prop('disabled',true);
                            });
                        }
                    }
                    if(data.funcs!=null){
                        $.each(data.funcs,function(index,item){
                            $formGroup.find(data.label).last().bind(item.event,item.func);
                        });
                    }
                });
            });

            $formBody.formValidation(validOption);
            return $formBody;
        };
        var $formBody=createForm($form.find('.form-horizontal'),option);
        var formValidation=$formBody.data('formValidation');
        this.add=function(){
            formValidation.resetForm();
            $formBody[0].reset();
            $formBody.find('#tag').tagEditor('destroy');
            $formBody.find('#tag').tagEditor();
        };
        this.update=function(data){
            formValidation.resetForm();
            $formBody.find('.check').prop('disabled',true);
            $.each($formBody.find('[name]'),function(index,item){
                var type=$(item).prop('type');
                var name=$(item).prop('name');
                var val;
                if(type=='radio')
                    $(item).filter('[value='+data[name]+']').prop("checked",true);
                else
                if(type=='checkbox'&&data[name]!=null){
                    $.each(data[name].split(','),function(i,data){
                        $(item).filter('[value='+data+']').prop("checked",true);
                    })
                }
                else {
                    if(typeof data[name]=='string') {
                        val = data[name].trim();
                        $(item).val(val);
                    }
                    else
                        $(item).val(data[name]);
                }
            });
            $formBody.find('#tag').tagEditor('destroy');
            $formBody.find('#tag').tagEditor();
        };
        this.save=function(){
            formValidation.validate();
            if(formValidation.isValid()) {
                var data={disabled:[]};
                var checkbox=null;
                $.each($formBody.find('[name]'),function(index,item){
                    if($(item).prop('disabled')||$(item).val()=='')
                        data.disabled.push($(item).prop('name'));
                    else{
                        var type=$(item).prop('type');
                        if(type=='radio') {
                            if($(item).prop('checked'))
                                data[$(item).prop('name')] = $(item).val();
                        }
                        else
                        if(type=='checkbox'){
                            if($(item).prop('checked')){
                                if(checkbox==null)
                                    checkbox = $(item).val();
                                else
                                    checkbox += ','+$(item).val();
                            }
                            data[$(item).prop('name')] = checkbox;
                        }
                        else
                            data[$(item).prop('name')] = $(item).val().trim();
                    }
                });
                return data;
            }
        };
        this.getElementsByName=function(name){
            return $formBody.find('[name='+name+']');
        };
        this.getElementsByClass=function(classname){
            return $formBody.find('.'+classname);
        };
        this.getElementsById=function(idname){
            return $formBody.find('#'+idname);
        };
        this.getElementsByLabel=function(labelname){
            return $formBody.find(labelname);
        };
        this.getContainer=function(){
            return $formBody;
        };
        this.hideItemByName=function(name){
            $formBody.find('[name='+name+']').closest('.form-group').hide();
        };
        this.showItemByName=function(name){
            $formBody.find('[name='+name+']').closest('.form-group').show();
        };
    };
    makeModal=function($container,title,addclass){
        this.modalString=
            '<div class="modal fade">'
            +'<div class="modal-dialog">'
            +'<div class="modal-content">'
            +'<div class="modal-header">'
            +'<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="mdi-navigation-cancel text-warning"></i></button>'
            +'<h4 class="modal-title text-center"></h4>'
            +'</div>'
            +'<div class="modal-body">'
            +'</div>'
            +'<div class="modal-footer">'
            +'<button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>'
            +'<button type="button" class="btn btn-success" id="save">确定</button>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'</div>';
        $container.append(this.modalString);
        $container.find('.modal-dialog:last').addClass(addclass);
        this.$modal=$container.find('.modal').last();
        this.$modal.find('.modal-title').html(title);
        this.$modal.on('hidden.bs.modal', function () {
            $(this).find('#save').unbind('click');
            $(this).find('[data-dismiss]').unbind('click');
        });
        this.show=function(yesfunc,nofunc){
            this.$modal.modal('show');
            this.$modal.find('#save').click(yesfunc);
            this.$modal.find('[data-dismiss]').click(nofunc);
        };
        this.hide=function(){
            this.$modal.modal('hide');
        };
        this.confirm=function(title,content,yesfunc,nofunc){
            this.$modal.find('.modal-title').html(title);
            this.$modal.find('.modal-body').html(content);
            this.$modal.modal('show');
            this.$modal.find('#save').click(yesfunc);
            this.$modal.find('[data-dismiss]').click(nofunc);
        };
        this.getElementsByName=function(name){
            return this.$modal.find('[name='+name+']');
        };
        this.getElementsByClass=function(classname){
            return this.$modal.find('.'+classname+'');
        };
        this.getElementsById=function(id){
            return this.$modal.find('#'+id+'');
        };
        this.setTitle=function (title) {
            this.$modal.find('.modal-title').html(title);
        };
        this.setBody=function(html){
            this.$modal.find('.modal-body').html(html);
        };
        this.hideFooter=function(){
            this.$modal.find('.modal-footer').hide();
        };
    };
    makeCircleTile=function($container,target,image,text,number,color){
        this.tileString= '<div class="circle-tile">'
            +'<a>'
            +'<div class="circle-tile-heading '+color+'">'
            +'<i class="fa fa-'+image+' fa-fw fa-3x"></i>'
            +'</div>'
            +'</a>'
            +'<div class="circle-tile-content '+color+'">'
            +'<div class="circle-tile-description text-faded">'
            +text
            +'</div>'
            +'<div class="circle-tile-number text-faded">'
            +number
            +'</div>'
            +'<a id="'+target+'" class="circle-tile-footer">更多信息 <i class="fa fa-chevron-circle-right"></i></a>'
            +'</div>'
            +'</div>';
        $container.append(this.tileString);
        $container.find('#'+target).click(function(){ $.gevent.publish('loadpage',$(this).attr('id'));});
        this.$circletile=$container.find('.circle-tile').last();
        this.setTarget=function(target){
            this.$circletile.find('a').prop('id',target);
        };
        this.setText=function(text){
            this.$circletile.find('.circle-tile-description').html(text);
        };
        this.setNumber=function(number){
            this.$circletile.find('.circle-tile-number').html(number);
        };
    };
    makePortlet=function($container,title,color,widgets,body){
        this.protletString= '<div class="portlet portlet-'+color+'">'
            +'<div class="portlet-heading">'
            +'<div class="portlet-title">'
            +'<h4><nobr>'+title+'</nobr></h4>'
            +'</div>'
            +'<div class="portlet-widgets">'
            +widgets
            +'</div>'
            +'<div class="clearfix"></div>'
            +'</div>'
            +'<div class="portlet-body">'
            +body
            +'</div>'
            +'</div>';
        $container.append(this.protletString);
        this.$portlet=$container.find('.portlet').last();
        this.setTitle=function(title){
            this.$portlet.find('.portlet-title h4').html(title);
        };
        this.setWidgets=function(widgets){
            this.$portlet.find('.portlet-widgets').html(widgets);
        };
        this.setBody=function(body){
            this.$portlet.find('.portlet-body').html(body);
        };
        this.hide=function(){
            $container.hide();
        };
        this.show=function(){
            $container.show();
        };
    };
    popover={
        show:function($container,message){
            var option={
                delay:{"hide": 100},
                placement:'top',
                trigger:'focus'
            };
            option['content']=message;
            $container.popover(option);
            $container.popover('show');
            setTimeout(function(){$container.popover('destroy');},3000);
        }
    };
    return{
        makeModel:makeModel,
        makeAtm:makeAtm,
        makeTable:makeTable,
        makeTree:makeTree,
        makeChart:makeChart,
        makeForm:makeForm,
        popover:popover,
        makeModal:makeModal,
        makeDateRange:makeDateRange,
        makeDate:makeDate,
        makeCircleTile:makeCircleTile,
        makePortlet:makePortlet
    };
}());