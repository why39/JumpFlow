var neo = new Neo(connection);
var name_list = [];
var arr = new Array();
arr["刑事检察"] = 0;
arr["刑事执行"] = 0;
arr["公益诉讼"] = 0;
arr["民事"] = 0;
arr["行政"] = 0;
arr["未检业务"] = 0;
arr["控告申诉"] = 0;
arr["检委办"] = 0;
arr["对台业务"] = 0;
arr["案管"] = 0;
arr["司法协助"] = 0;
name_list.push("刑事检察");
name_list.push("刑事执行");
name_list.push("公益诉讼");
name_list.push("民事");
name_list.push("行政");
name_list.push("未检业务");
name_list.push("控告申诉");
name_list.push("检委办");
name_list.push("对台业务");
name_list.push("案管");
name_list.push("司法协助");
try {
    var query = "MATCH (m)-[]-(n:USER) WHERE n.name = '" + jiema2() + "' AND m.CN_KEY IS NOT NULL WITH m MATCH (x:CASE) WHERE x.caseId = m.caseId RETURN x.案件类别";
    neo.executeQuery(query, {}, function (err, res) {
        if (res.table) {
            for (index in res.table) {
                var group_id = res.table[index]["x.案件类别"];
                console.log(group_id);
                if (group_id) {
                    arr[group_id]++;
                }
            }

            setTimeout(function(){
                !function ($) {
                    "use strict1";

                    var Dashboard = function () {
                    };

                    //creates Stacked chart
                    Dashboard.prototype.createStackedChart  = function(element, data, xkey, ykeys, labels, lineColors) {
                        Morris.Bar({
                            element: element,
                            data: data,
                            xkey: xkey,
                            ykeys: ykeys,
                            stacked: true,
                            labels: labels,
                            hideHover: 'auto',
                            resize: true, //defaulted to true
                            gridLineColor: '#eeeeee',
                            barColors: lineColors,
                            labelTop:true,
                            barRatio: 0.4,
                            xLabelAngle: 35,
                        });
                    },
                        // pie
                        $('.peity-pie').each(function () {
                            $(this).peity("pie", $(this).data());
                        });

                    //donut
                    $('.peity-donut').each(function () {
                        $(this).peity("donut", $(this).data());
                    });

                    // line
                    $('.peity-line').each(function () {
                        $(this).peity("line", $(this).data());
                    });

                    Dashboard.prototype.init = function () {
                        var $stckedData  = [
                        ];
                        for(var i = 0;i < name_list.length;i++){
                            $stckedData.push({
                                y: name_list[i],
                                a: arr.name_list[i]
                            })
                        }
                        this.createStackedChart('morris-bar-stacked1', $stckedData, 'y', ['a'], ['案件类别数量'], ['#1699dd']);
                    },
                        //init
                        $.Dashboard = new Dashboard, $.Dashboard.Constructor = Dashboard
                }(window.jQuery),
                    function ($) {
                        "use strict1";
                        $.Dashboard.init();
                    }(window.jQuery);
            },500)
        }
    });
}catch (e) {}