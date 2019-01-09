$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/oss/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', width: 20, key: true},
            {label: 'URL地址', name: 'url', width: 160},
            {label: '创建时间', name: 'createDate', width: 40},
            {label: '操作', name: '操作', width: 30, formatter: editLink}

        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function editLink(cellValue, options, rowdata, action) {

        return "<a href='http://localhost:8080/hxyActiviti/sys/oss/downloadFile?url=" + rowdata.url + "' target='_blank'>下载</a>";

    }


    new AjaxUpload('#upload', {
        action: '../sys/oss/fileupload',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            return true;
        },
        onComplete: function (file, r) {
            if (r.code == 0) {
                alert(r.url);
                vm.reload();
            } else {
                alert(r.msg);
            }
        }
    });

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        config: {}
    },
    created: function () {
        this.getConfig();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        getConfig: function () {
            $.getJSON("../sys/oss/config", function (r) {
                vm.config = r.config;
            });
        },
        addConfig: function () {
            vm.showList = false;
            vm.title = "云存储配置";
        },
        saveOrUpdate: function () {
            var url = "../sys/oss/saveConfig";
            $.ajax({
                type: "POST",
                url: url,
                contentType: "application/json",
                data: JSON.stringify(vm.config),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function () {
            var ossIds = getSelectedRows();
            if (ossIds == null) {
                return;
            }

            confirm('确定要删除选中的文件？', function () {
                $.ajax({
                    type: "POST",
                    url: "../sys/oss/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ossIds),
                    success: function (r) {
                        if (r.code === 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                            vm.reload();
                        }
                    }
                });
            });
        },

        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});