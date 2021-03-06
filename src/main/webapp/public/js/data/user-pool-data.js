var oTableData = $('#dt-user-data-pool')
		.DataTable(
				{
					"dom" : "t"
							+ "<'dt-toolbar-footer'<'col-sm-6 col-xs-12'i><'col-xs-12 col-sm-6 text-right'lp>>",
					"autoWidth" : false,
					"ordering" : true,
					"serverSide" : true,
					"processing" : true,
					"searching" : false,
					"pageLength" : 10,
					"lengthMenu" : [ 10, 15, 20, 25, 30 ],
					"infoCallback":function(settings, start, end, max, total, pre){
				    	return "显示第 " + start + " 至 " + end + " 项结果，共 " + total + " 项，已选中 " + getCheckNum() + " 项";
				    },
					"language" : {
						"url" : getContext() + "public/dataTables.cn.txt"
					},
					"ajax" : {
						"url" : getContext() + "userpooldata/pooldata",
						"type" : "POST",
						"data" : function(param) {
							param.itemTable = $("#dataPoolTable").val();
							param.itemName = $("#itemName").val();
							param.itemPhone = $("#itemPhone").val();
							param.itemJson = $("#itemJson").val();
							param.itemOwner = $("#itemOwner").val();
							param.callTimes = $("#callTimes").val();
						}
					},
					"paging" : true,
					"pagingType" : "bootstrap",
					"lengthChange" : true,
					"order" : [ [ "1", "desc" ] ],
					"columns" : [
							{
								"width": "10%",
								"title" : '<div class="btn-group">'
										+ ' <a class="btn btn-primary" href="javascript:void(0);" onclick="docheckall()">全选</a>'
										+ ' <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);"><span class="caret"></span></a><ul class="dropdown-menu">'
										+ ' <li><a href="javascript:void(0);"onclick="docheckall()" >全选所有</a></li>'
										+ '<li><a href="javascript:void(0);" onclick="docheckallpage()">全选本页</a></li>'
										+ '<li><a href="javascript:void(0);" onclick="docancelAll()">取消所有选择</a></li>'
										+ '</ul></div>',
								"sortable" : false,
								"data" : null,
								"render" : function(data, type, full) {
									if (itemCheckList[full.uid]) {
										return '<input type="checkbox" checked="checked" id="dck'
												+ full.uid
												+ '" onclick="docheck('
												+ "'" + full.uid + "'" +  ')"  />';
									} else {
										return '<input type="checkbox" id="dck'
												+ full.uid
												+ '" onclick="docheck('
												+ "'" + full.uid + "'" +  ')"  />';
									}
								}
							},
							{ "title" : "条目名称","data" : "itemName","defaultContent":"无名"},
							{ "title" : "条目号码","data" : "itemPhone","defaultContent":"无号码"},
							{ "title" : "条目地址","data" : "itemAddress","defaultContent":"无地址"},
							{ "title" : "条目其它信息","data" : "itemJson","defaultContent":"无其它信息"},
//							{ "title" : "条目归属人","data" : "itemOwner","defaultContent":"无"},
//							{ "title" : "呼叫次数","data" : "callTimes","defaultContent":"无"},
							{ "title" : "分配时间","data" : "allocateTime","defaultContent":"未分配"},
							{
								"title" : "操作",
								"data" : "null",
								"render" : function(data, type, full) {
									return  "<a onclick='getback(\""
											+ full.uid + "\");'>取回</a>";
								}
							} ],
				});
