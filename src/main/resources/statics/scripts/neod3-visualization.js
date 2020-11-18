var over_bmsah = null;
function Neod3Renderer() {

    var styleContents =
        "node {\
          diameter: 40px;\
          color: #DFE1E3;\
          border-color: #D4D6D7;\
          border-width: 2px;\
          text-color-internal: #000000;\
          text-color-external: #000000;\
          caption: '{name}';\
          font-size: 12px;\
        }\
        relationship {\
          color: #4356C0;\
          shaft-width: 3px;\
          font-size: 9px;\
          padding: 3px;\
          text-color-external: #000000;\
          text-color-internal: #FFFFFF;\
        }\n";

    var skip = ["id", "start", "end", "source", "target", "labels", "type", "selected", "properties"];
    var prio_props = ["name", "title", "tag", "username", "lastname", "caption"];

    var serializer = null;

    var $downloadSvgLink = $('<a href="#" class="btn btn-success visualization-download" target="_blank"><i class="icon-download-alt"></i> Download SVG</a>').hide().click(function () {
        $downloadSvgLink.hide();
    });
    var downloadSvgLink = $downloadSvgLink[0];
    var blobSupport = 'Blob' in window;
    var URLSupport = 'URL' in window && 'createObjectURL' in window.URL;
    var msBlobSupport = typeof window.navigator.msSaveOrOpenBlob !== 'undefined';
    var svgStyling = '<style>\ntext{font-family:sans-serif}\n</style>';
    //var stylingUrl = window.location.hostname === 'www.neo4j.org' ? 'http://gist.neo4j.org/css/neod3' : 'styles/neod3';
    var stylingUrl = 'http://localhost:8083/hxyActiviti/styles/neod3';
    if (window.isInternetExplorer) {
        stylingUrl += '-ie.css';
    } else {
        stylingUrl += '.css';
    }

    var existingStyles = {};
    var currentColor = 1;

    var selectNode = null;

    function clickNode(node) {
        console.log("click_node");
        selectNode = JSON.parse(JSON.stringify(node));
        console.log(selectNode);
        if(selectNode.name != null) {
            var neo = new Neo(connection);
            try {
            query = "MATCH (n)-[r]->(m) RETURN n,r,m";
            neo.executeQuery(query, {}, function (err, res) {
                res = res || {};
                var graph = res.graph;
                if (graph) {
                    var node = graph.nodes;
                    selectNode = JSON.parse(JSON.stringify(node));
                    //console.log(selectNode);
                    if (selectNode){
                        for(var i in selectNode) {
                            if(selectNode[i].CN_KEY != null){
                                //console.log(selectNode[i].CN_KEY);
                            }
                        }
                    }
                } else {
                    if (err) {
                        console.log(err);
                    }
                }
            });
        }catch (e) {
            console.log(e);
        }
            //document.getElementById("suyuanName").value = '附注';
        }
        if (selectNode.label == "Task") {
            $(".nodeId").text("<id> : "+node.id);
            $(".caseId").text("<所属案件ID> : "+selectNode.caseId);
            // $(".nodeTaskId").text("<案件环节ID> : "+selectNode.taskId);
            $(".nodeName").text("<环节名称> : "+selectNode.name);
            document.getElementById("suyuanDataId").innerHTML = "<p style='margin-left:5px;color:#ff6601;'>ID:"+node.id+"&nbsp&nbsp&nbsp&nbsp案件ID:" +
                ""+selectNode.caseId+"&nbsp&nbsp&nbsp&nbsp环节名称:"+selectNode.copy_name+"</p>";
        } else {
            var alertString ="";
            var alertString1 =""
            for(var k in selectNode){//遍历json对象的每个key/value对,k为key
                if("CN_KEY" != k && "label" !=k && "copy_name"!=k && selectNode["label"]!=k && "CaseNodeId"!=k && "lastNodeId"!=k){
                    if(k == 'name') {
                        selectNode[k] = selectNode['copy_name'];
                    }
                    alertString+=k+" : " +selectNode[k]+"\n";
                    alertString1+="<span style='color:pink'>k<span>+selectNode[k]";
                }
            }
            if(selectNode["label"] ==  undefined) {
                selectNode["label"] == "";
                swal({
                    title: selectNode["copy_name"],
                    text: alertString,
                    icon: "success",
                    animation: "slide-from-top"
                });
                /*sweetAlert(selectNode["copy_name"]
                    ,alertString
                    , "info");*/
            }
            else {
                sweetAlert(selectNode["copy_name"] + ":" + selectNode["label"]
                    ,alertString
                    , "success");
            }
            //console.log(selectNode["label"]);

            document.getElementById("suyuanDataId").innerHTML = "<p style='margin-left:5px;color:#ff6601;'>"+alertString+"</p>";
        }

        //$(".nodeFile").text("文案材料 : "+selectNode.file);


    }

    function dummyFunc(node) {

    }


    function render(id, $container, visualization) {
        console.log("render");
        function extract_props(pc) {
            var p = {};
            for (var key in pc) {
                if (!pc.hasOwnProperty(key) || skip.indexOf(key) != -1) continue;
                p[key] = pc[key];
            }
            return p;
        }

        function node_styles(nodes) {
            function label(n) {
                var labels = n["labels"];
                if (labels && labels.length) {
                    return labels[labels.length - 1];
                }
                return "";
            }

            console.log("node_styles");
            var style = {};
            for (var i = 0; i < nodes.length; i++) {
                var props = nodes[i].properties = extract_props(nodes[i]);
                var keys = Object.keys(props);
                if (label(nodes[i]) !== "" && keys.length > 0) {
                    var selected_keys = prio_props.filter(function (k) {
                        return keys.indexOf(k) !== -1
                    });
                    selected_keys = selected_keys.concat(keys).concat(['id']);
                    var selector = "node." + label(nodes[i]);
                    var selectedKey = selected_keys[0];
                    if (typeof(props[selectedKey]) === "string") {
                        //console.log(props[selectedKey] + "   "+ selectedKey)
                        if(selectedKey == 'name') {
                            props['copy_name'] = props[selectedKey];
                            if (props[selectedKey].length > 3) {
                                props[selectedKey] = props[selectedKey].substring(0, 3) + " ...";
                            }

                        }else {
                            props[selectedKey] = props[selectedKey];
                        }
                    }
                    style[selector] = style[selector] || selectedKey;
                }
            }
            return style;
        }

        function style_sheet(styles, styleContents) {
            function format(key) {
                var item = styles[key];
                return item.selector +
                    " {caption: '{" + item.caption +
                    "}'; color: " + item.color +
                    "; border-color: " + item['border-color'] +
                    "; text-color-internal: " + item['text-color-internal'] +
                    "; text-color-external: " + item['text-color-external'] +
                    "; }"
            }

            return styleContents + Object.keys(styles).map(format).join("\n");
        }

        function create_styles(styleCaptions, styles) {
            console.log("create_styles");
            var colors = neo.style.defaults.colors;
            for (var selector in styleCaptions) {
                if (!(selector in styles)) {
                    var color = colors[currentColor];
                    currentColor = (currentColor + 1) % colors.length;
                    var textColor = window.isInternetExplorer ? '#000000' : color['text-color-internal'];
                    var style = {
                        selector: selector,
                        caption: styleCaptions[selector],
                        color: color.color,
                        "border-color": color['border-color'],
                        "text-color-internal": textColor,
                        "text-color-external": textColor
                    }
                    styles[selector] = style;
                }
            }
            return styles;
        }

        function applyZoom() {
            console.log("applyZoom");
            renderer.select(".nodes").attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
            renderer.select(".relationships").attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        function enableZoomHandlers() {
            renderer.on("wheel.zoom", zoomHandlers.wheel);
            renderer.on("mousewheel.zoom", zoomHandlers.mousewheel);
            renderer.on("mousedown.zoom", zoomHandlers.mousedown);
            renderer.on("DOMMouseScroll.zoom", zoomHandlers.DOMMouseScroll);
            renderer.on("touchstart.zoom", zoomHandlers.touchstart);
            renderer.on("touchmove.zoom", zoomHandlers.touchmove);
            renderer.on("touchend.zoom", zoomHandlers.touchend);
        }

        function disableZoomHandlers() {
            renderer.on("wheel.zoom", null);
            renderer.on("mousewheel.zoom", null);
            renderer.on("mousedown.zoom", null);
            renderer.on("DOMMouseScroll.zoom", null);
            renderer.on("touchstart.zoom", null);
            renderer.on("touchmove.zoom", null);
            renderer.on("touchend.zoom", null);
        }

        function legend(svg, styles) {
            var keys = Object.keys(styles).sort();
            var circles = svg.selectAll('circle.legend').data(keys);
            var r = 20;
            circles.enter().append('circle').classed('legend', true).attr({
                cx: 2 * r,
                r: r
            });
            circles.attr({
                cy: function (node) {
                    return (keys.indexOf(node) + 1) * 2.2 * r;
                },
                fill: function (node) {
                    return styles[node]['color'];
                },
                stroke: function (node) {
                    return styles[node]['border-color'];
                },
                'stroke-width': function (node) {
                    return "2px";
                }
            });
            var text = svg.selectAll('text.legend').data(keys);
            text.enter().append('text').classed('legend', true).attr({
                'text-anchor': 'left',
                'font-weight': 'bold',
                'stroke-width': '0',
                'stroke-color': 'black',
                'fill': 'black',
                'x': 3.2 * r,
                'font-size': "12px"
            });
            text.text(function (node) {
                var label = styles[node].selector;
                return label ? label.substring(5) : "";
            }).attr('y', function (node) {
                return (keys.indexOf(node) + 1) * 2.2 * r + 6;
            })
            /*
                      .attr('stroke', function(node) {
                        return styles[node]['color'];
                      })
                     .attr('fill', function(node) {
                          return styles[node]['text-color-internal'];
                      });
            */
            return circles.exit().remove();
        }

        function keyHandler() {
            if (d3.event.altKey || d3.event.shiftKey) {
                enableZoomHandlers();
            }
            else {
                disableZoomHandlers();
            }
        }

        console.log("KeyHandler");
        var links = visualization.links;
        var nodes = visualization.nodes;
        for (var i = 0; i < links.length; i++) {
            links[i].source = links[i].start;
            links[i].target = links[i].end;
            //  links[i].properties = props(links[i]);
        }
        var nodeStyles = node_styles(nodes);
        create_styles(nodeStyles, existingStyles);
        var styleSheet = style_sheet(existingStyles, styleContents);
        var graphModel = neo.graphModel()
            .nodes(nodes)
            .relationships(links);
        var graphView = neo.graphView()
            .style(styleSheet)
            .width($container.width()).height($container.height()).on('nodeClicked', clickNode).on('relationshipClicked', dummyFunc).on('nodeDblClicked', dummyFunc);
        var svg = d3.select("#" + id).append("svg");
        var renderer = svg.data([graphModel]);
        // legend(svg, existingStyles);
        var zoomHandlers = {};
        var zoomBehavior = d3.behavior.zoom().on("zoom", applyZoom).scaleExtent([0.01, 2]);
        console.log("zoom" + zoomBehavior);

        //renderer.call(zoomBehavior);
        renderer.call(graphView);


        zoomHandlers.wheel = renderer.on("wheel.zoom");
        zoomHandlers.mousewheel = renderer.on("mousewheel.zoom");
        zoomHandlers.mousedown = renderer.on("mousedown.zoom");
        zoomHandlers.DOMMouseScroll = renderer.on("DOMMouseScroll.zoom");
        zoomHandlers.touchstart = renderer.on("touchstart.zoom");
        zoomHandlers.touchmove = renderer.on("touchmove.zoom")
        zoomHandlers.touchend = renderer.on("touchend.zoom");
        disableZoomHandlers();

        d3.select('body').on("keydown", keyHandler).on("keyup", keyHandler);
        console.log("d3_keyHandler");

        function refresh() {
            graphView.height($container.height());
            graphView.width($container.width());
            renderer.call(graphView);
        }

        function saveToSvg() {
            var svgElement = $('#' + id).children('svg').first()[0];
            var xml = serializeSvg(svgElement, $container);
            if (!msBlobSupport && downloadSvgLink.href !== '#') {
                window.URL.revokeObjectURL(downloadSvgLink.href);
            }
            var blob = new window.Blob([xml], {
                'type': 'image/svg+xml'
            });
            var fileName = id + '.svg';
            if (!msBlobSupport) {
                downloadSvgLink.href = window.URL.createObjectURL(blob);
                $downloadSvgLink.appendTo($container).show();
                $downloadSvgLink.attr('download', fileName);
            } else {
                window.navigator.msSaveOrOpenBlob(blob, fileName);
            }
        }

        function getFunctions() {
            var funcs = {};
            if (blobSupport && (URLSupport || msBlobSupport)) {
                funcs['icon-download-alt'] = {'title': 'Save as SVG', 'func': saveToSvg};
            }
            return funcs;
        }

        return {
            'subscriptions': {
                'expand': refresh,
                'contract': refresh,
                'sizeChange': refresh
            },
            'actions': getFunctions()
        };
    }

    function serializeSvg(element, $container) {
        if (serializer === null) {
            if (typeof window.XMLSerializer !== 'undefined') {
                var xmlSerializer = new XMLSerializer();
                serializer = function (emnt) {
                    return xmlSerializer.serializeToString(emnt);
                };
            } else {
                serializer = function (emnt) {
                    return '<svg xmlns="http://www.w3.org/2000/svg">' + $(emnt).html() + '</svg>';
                }
            }
        }
        var svg = serializer(element);
        svg = svg.replace('<svg ', '<svg height="' + $container.height() + '" width="' + $container.width() + '" ')
            .replace(/<g/, '\n' + svgStyling + '\n<g');
        return svg;
    }

    $.get(stylingUrl, function (data) {
        svgStyling = '<style>\n' + data + '\n</style>';
        $(svgStyling).appendTo('head');
    });

    return {'render': render};
}

function lineageSelect(bmsah) {
    over_bmsah = bmsah;
    window.location.href="http://localhost:8083/hxyActiviti/neoData.html?bmsah="+encodeURI(bmsah);
}


