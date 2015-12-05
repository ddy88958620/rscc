<div class="modal fade" id="dialog_getBlackListCount">
	<div class="modal-dialog" style="width: 90%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><strong>关闭</strong></button>
				<h4 class="modal-title">黑名单号码量数据查看</h4>
			</div>
			<div class="modal-body">
				<section id="widget-grid-getBlackListCount" class="">
					<div class="row">
						<article class="col-lg-12 col-lg-12 col-lg-12 col-lg-12">
							<div style="margin-bottom: 8px;">
							</div>
							<div class="jarviswidget jarviswidget-color-darken" data-widget-editbutton="false">
								<header>
									<span class="widget-icon"> <i class="fa fa-table"></i>
									</span>
									<h2>数据信息</h2>
								</header>
								<div>
									<div class="widget-body no-padding">
	<!-- 									<div class="widget-body-toolbar"></div> -->
										<input type="hidden" name="batchUuid" id="batchUuid" value="${(batchUuid)!''}" />
										<input type="hidden" name="deptUuid" id="deptUuid" value="${(deptUuid)!''}" />
										<table id="dt_basic_getBlackListcount" class="table table-bordered table-hover"></table>
									</div>
								</div>
							</div>
						</article>
					</div>
				</section>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	$('#dialog_getBlackListCount').modal("show");
	
	$('#dialog_getBlackListCount').on('hide.bs.modal', function (e) { 
		$("#dialog_getBlackListCount").remove();
	});

</script>

<script src="${springMacroRequestContext.contextPath}/public/js/databatch/getBlackListCount.js"></script>


