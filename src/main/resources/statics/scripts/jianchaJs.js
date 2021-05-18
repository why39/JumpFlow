var over_group = ['刑事检察', '刑事执行', '公益诉讼', '民事', '行政', '未检业务', '控告申诉', '检委办', '对台业务', '案管', '司法协助'];
var over_time = ['朱某某逃逸案', '周某某盗窃案',
    '张某某盗窃案', '郭某某漏税案', '刘某某投毒案', '贺某某纵火案', '王某某强奸案', '李某某贪污案'];




var connection = function () {
    configStr = localStorage.getItem("neoconfig");
    configJSON = JSON.parse(configStr)
    // console.log(configJSON.NEO_SERVER_URL, configJSON.NEO_SERVER_USER, configJSON.NEO_SERVER_PSW)
    config = {
        url: "http://" + configJSON.NEO_SERVER_URL + ":7474",
        user: configJSON.NEO_SERVER_USER,
        pass: configJSON.NEO_SERVER_PSW
    };
    return config;
}


function execute(content) {
    var neod3 = new Neod3Renderer();

    var neo = new Neo(connection);
    try {
        var query = content;
        //console.log("Executing Query", query);
        neo.executeQuery(query, {}, function (err, res) {
            res = res || {}
            var graph = res.graph;
            if (graph) {
                var c = $("#graph");
                c.empty();
                neod3.render("graph", c, graph);
            } else {
                if (err) {
                    console.log(err);
                    if (err.length > 0) {
                        sweetAlert("Cypher error", err[0].code + "\n" + err[0].message, "error");
                    } else {
                        sweetAlert("Ajax " + err.statusText, "Status " + err.status + ": " + err.state(), "error");
                    }
                }
            }
        });
    } catch (e) {
        console.log(e);
        sweetAlert("Catched error", e, "error");
    }
}

function trace() {
    var query = null;
    if ($("#caseTaskId").val()) {
        query = "MATCH (n) WHERE ID(n)=" + $("#caseTaskId").val() + " WITH n MATCH p = (m) - [*1.." + $("#actionLength").val() + "] -> (n) RETURN n,p";//$("#caseTaskId").val();
    } else {
        query = "  MATCH (n)-[r]->(m) RETURN n,r,m";
    }
    console.log("$(\"#caseTaskId\").val()---->" + $("#caseTaskId").val());
    clearNodeInfo();
    execute(query);
}

function inference() {
    var query = null;
    if ($("#caseTaskId").val()) {
        query = "MATCH (n) WHERE ID(n)=" + $("#caseTaskId").val() + " WITH n MATCH p = (n) - [*1.." + $("#actionLength").val() + "] -> (m) RETURN n,p";//$("#caseTaskId").val();
    } else {
        query = "  MATCH (n)-[r]->(m) RETURN n,r,m";
    }
    console.log("$(\"#caseTaskId\").val()---->" + $("#caseTaskId").val());
    clearNodeInfo();
    execute(query);
}

function executeQuery() {
    execute($("#executeQuery").val());
}

function executeClear() {
    execute("match(n)-[r]-(m) delete r,n,m");
}

function logImport() {
    var url = "${webRoot}/logImport";
    var params = {
        'caseId': $("#logIn").val(),
    };
    $.post(url, params, function (result) {
        if (result.code == '0') {
            console.log(result);
        } else {
            alertMsg(result.msg);
        }
    });
}

function clearNodeInfo() {

    //neo pane区域
    $(".nodeId").text("");
    $(".caseId").text("");
    $(".nodeName").text("");
}

function executeNode() {
    var neo = new Neo(connection);
    try {
        var query = "MATCH (n:operator) WHERE n.operator<> \"\" AND n.operator<> \"null\"  RETURN DISTINCT n.operator";
        var len = window.parent.document.getElementById("index-user-name").innerHTML.length;
        var user_name = window.parent.document.getElementById("index-user-name").innerHTML.substring(3,len)
        neo.executeQuery(query, {}, function (err, res) {
            $.ajax({
                type: "get",
                async: false,
                url: "/hxyActiviti/demo/gl/list",
                success: function (ajax_res) {
                    var flag = 0;
                    for(var i = 0;i < ajax_res.length;i++){
                        if(ajax_res[i].group == user_name){
                            flag = ajax_res[i].member;
                        }
                    }
                    if (res.table) {
                        if(flag == 0) {
                            for(index in res.table) {
                                if(res.table[index]["n.operator"] == user_name){
                                    str = "<li class=\"list-group-item\" style=\"height: 45px;\"><span style=\"float: left;margin-top:-2px;width:250px;text-align:left;\">" + res.table[index]["n.operator"] + "</span> "
                                        + "<button style=\"margin-left: 6px;float: right;margin-top:-6px\" type=\"button\" class=\"btn btn-primary\" onclick=\"lineageSelect1(\'" + res.table[index]["n.operator"] + "\')\">" + "世系查询" + "</button> ";
                                    document.getElementById("caseList").innerHTML += str;
                                }
                            }
                        }
                        if(flag == 1) {
                            for(var i = 0;i < ajax_res.length;i++){
                                if(ajax_res[i].group != "超级管理员"){
                                    str = "<li class=\"list-group-item\" style=\"height: 45px;\"><span style=\"float: left;margin-top:-2px;width:250px;text-align:left;\">" + ajax_res[i].group + "</span> "
                                        + "<button style=\"margin-left: 6px;float: right;margin-top:-6px\" type=\"button\" class=\"btn btn-primary\" onclick=\"lineageSelect1(\'" + ajax_res[i].group + "\')\">" + "世系查询" + "</button> ";
                                    document.getElementById("caseList").innerHTML += str;
                                }
                            }

                        }
                        var zz = getzz();
                        change(1, zz);
                    }
                }
            });
        });;
    } catch (e) {
    }
   /* $.ajax({
        type: "get",
        async: false,
        url: "/hxyActiviti/demo/gl/list",
        success: function (res) {
            if (res) {
                for (index in res) {
                    group_id = res[index].group_id;
                    str = "<li class=\"list-group-item\" style=\"height: 45px;\"><span style=\"float: left;margin-top:-2px;width:250px;text-align:left;\">" + res[index].group + "</span> "
                        + "<button style=\"margin-left: 6px;float: right;margin-top:-6px\" type=\"button\" class=\"btn btn-primary\" onclick=\"lineageSelect1(\'" + "民行团队" + "\')\">" + "世系查询" + "</button> "
                        + "<button onclick=\"MemberName(\'" + group_id + "\')\" style=\"float:right;margin-right:430px;margin-top:-2px;  background-color: transparent;" +
                        "border: 0px solid transparent;outline: none;\">" + "团队成员详情" + "</button>";
                    document.getElementById("caseList").innerHTML += str;
                }
                var zz = getzz();
                change(1, zz);
            }
        }
    });*/
}

function getzz() {
    var a = $("ul#caseList li");
    var zz = new Array(a.length);
    for (var i = 0; i < a.length; i++) {
        zz[i] = a[i].innerHTML;
    } //div的字符串数组付给zz
    // console.log("zz is" + zz);
    return zz;
}


var pageno = 1;
var gg;

function change(e, zz) {
    //console.log("zz is" + zz);
    gg = zz;
    pageno = e;
    var pagesize = 4; //每页多少条信息
    if (zz.length % pagesize == 0) {
        var pageall = zz.length / pagesize;
    } else {
        var pageall = parseInt(zz.length / pagesize) + 1;
    }   //一共多少页
    if (e < 1) {
        e = 1;
        pageno = 1;//就等于第1页 ， 当前页为1
    }
    if (e > pageall) {  //如果输入页大于最大页
        e = pageall;
        pageno = pageall; //输入页和当前页都=最大页
    }
    $("#caseList").html("");//全部清空
    console.log("html is" + $("#caseList").html(""));
    var html = "";
    for (var i = 0; i < pagesize; i++) {
        if (zz[(e - 1) * pagesize + i] == null) zz[(e - 1) * pagesize + i] = "<span style=\"color:white\">空白</span>";
        html += '<li class=\"list-group-item\">' + zz[(e - 1) * pagesize + i] + '</li>';//创建一页的li列表
    }
    $("ul#caseList").html(html);//给ul列表写入html
    var ye = "";
    for (var j = 1; j <= pageall; j++) {
        if (e == j) {
            ye = ye + "<span><button onClick='change(" + j + ",gg)' style='color:#FF0000'>" + j + "</button></span> "
        } else {
            ye = ye + "<button onClick='change(" + j + ",gg)'>" + j + "</button>"
        }
    }
    var pageContent = "";
    pageContent += '第<span id=\"a2\">' + pageno + '</span>/';
    pageContent += '<span id="a1">' + pageall + '</span>页';
    pageContent += '<span id="a3">' + ye + '</span>';
    pageContent += '<button  onClick="change(--pageno,gg)">上一页</button>';
    pageContent += '<button onClick="change(++pageno,gg)">下一页</button>';
    $("#page").html(pageContent);
}

monitorClick();

function monitorClick() {
    document.getElementById("relevantCase").style.visibility = "visible";
    var ss = document.getElementById("xieshijiancha_a").innerHTML + "案件";
    var ll = document.getElementById("xieshijiancha_a").innerHTML;
    var query = null;
    query = "MATCH (n:BALB) WHERE n.class=\'" + "刑事检察" + "\' WITH n MATCH p = (n) - [*1] -> (m) RETURN m,p,n";
    clearNodeInfo();
    console.log("monitorClick");
    executeNode();
}
function MemberName(group_id){
    $.ajax({
        type: "get",
        async: false,
        url: "/hxyActiviti/demo/gn/list",
        success: function (r) {
            alertString = "";
            if (r) {
                for (i in r) {
                    if(group_id == r[i].group_id){
                        alertString +=r[i].type + " : " + r[i].member + "\n";
                    }
                }
                swal({
                    title: "团队信息",
                    text: alertString ,
                    icon: "success",
                });
            }
        }
    });
}

window.onload = (function () {
    //todo dynamic configuration
    // var query = "MATCH (n:CASE) WITH n MATCH p=(n)-[*]->(m:Task) RETURN p,n,m";
    // execute(query);
});


function IsUniqueString(str, name_str) {
    flag = 1;
    for (k in name_str) {
        if (str == name_str[k]) flag = 0;
    }
    return flag;
}

function IsMarkString(str, name_str) {
    flag = 0;
    for (k in name_str) {
        if (str == name_str[k]) flag = k;
        //console.log(str)
    }
    return flag;
}

function nameCount(name, name_count, name_str) {
    if (IsUniqueString(name, name_str)) {
        name_count.push(1);
        name_str.push(name);
        over_count++;
    } else {
        name_count[IsMarkString(name, name_str)] = name_count[IsMarkString(name, name_str)] + 1;
    }
    return name_count;
}


function toFIXED(num, n) {
    var numStr = num.toString()
    var index = numStr.indexOf('.')
    var result = numStr.slice(0, index + n)
    return Number(result);
}

function lineageSelect1(name){
    window.location.href="http://143.176.22.84:8083/hxyActiviti/group.html?name=" + encodeURI(name);
}



