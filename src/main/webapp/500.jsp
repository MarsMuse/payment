<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%    String path =request.getContextPath();  %>
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125"> <i
						class="ace-icon fa fa-random"></i> 500 </span> 出事了！
				</h1>

				<hr />
				<h3 class="lighter smaller">
					但我们正在努力 <i
						class="ace-icon fa fa-wrench icon-animated-wrench bigger-125"></i>
					它！
				</h3>
				
				<div class="accordion-group">
					<div class="accordion-heading">
						<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
							详细异常信息
						</a>
					</div>
					<div id="collapseOne" class="accordion-body collapse" style="height: 0px; ">
						<div class="accordion-inner">
							<h5>
								<%=request.getParameter("responseText")%>
							</h5>
						</div>
					</div>
				</div>
				
			</div>
		</div>
		
		<!-- /section:pages/error -->

		<!-- PAGE CONTENT ENDS -->
	</div>
	<!-- /.col -->
</div>
<!-- /.row -->
<style>
h1,h3,h5{
	background:none;
	color: #777777 !important;
}
</style>