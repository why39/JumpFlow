JumpFlow
====
A workflow system supporting jump to any activity
本项目基于检察业务系统背景，实现的是一个可以按自定义规则跳转的工作流系统
------------------------------------------------
##
Based on https://github.com/huangxianyuan/hxyFrame-activiti-boot<br>
在已有工作流的基础上，加入三大模块：<br>
###1.跳转模块<br>
	可以实现从当前环节到任意环节的跳转<br>
###2.文书模块<br>
	集成pageoffice插件的在线office编辑模块（不支持高版本office，WPS较为稳定，插件需要定期激活），可编辑文书模板，并使用文书模板生成文书实例<br>
###3.规则模块<br>
	创建文书模板和跳转的关联，生成相关文书模板的实例后就会触发跳转<br>
文书模板路径：\target\classes\statics\doc<br>
文书实例路径：\target\classes\statics\doc（目前尚未把不同流程实例的文书分开存储，待完善）<br>
pageoffice插件激活license路径：d:\IDE\lic<br>
