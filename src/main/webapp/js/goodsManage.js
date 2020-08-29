/**
 * Created by 文辉 on 2017/7/22.
 */
var activity = [];
var currentPage = 1;
$(document).ready(function () {

    var path = $("#path").text();


    to_page(path, 1);

});

/*$(document).ready(function () {
    $(".templatemo-edit-btn").click(function () {
        alert("asf");
        $("#update-goods").modal({
            backdrop:'static'
        })
    });
});*/

$(document).on("click",".description",function () {
    $(this).popover();
});

$(document).on("click",".templatemo-edit-btn",function () {
    $("#update-goods").modal({
        backdrop:'static'
    });

    //获取当前点击商品的数据
    var upgoodsId = $(this).parents("tr").find("td:eq(0)").text();
    var upgoodsName = $(this).parents("tr").find("td:eq(1)").text();
    var upGoodsPrice = $(this).parents("tr").find("td:eq(2)").text();
    var upGoodsNum = $(this).parents("tr").find("td:eq(3)").text();
    var upGoodsdetailCate = $(this).parents("tr").find("td:eq(4)").text();
    var upGoodsDes = $(this).parents("tr").find(".description").attr("data-content");

    $("#goodsId").text(upgoodsId);
    $("#goodsName").val(upgoodsName);
    $("#price").val(upGoodsPrice);
    $("#num").val(upGoodsNum);
    $("#detailCate").val(upGoodsdetailCate);
    $("#description").val(upGoodsDes);
});

//修改商品信息
$(document).on("click","#saveUpdate",function () {
    var ugoodsId = $("#goodsId").text();
    var ugoodsName = $("#goodsName").val();
    var uprice = $("#price").val();
    var unum = $("#num").val();
    var udescription = $("#description").val();
    var ucategory = $("#category").val();
    var udetailCate = $("#detailCate").val();

    /*var option = {
        url: '//admin/goods/update/'+goodsId,
        type:'post',
    };
    $("#update-goods").ajaxForm(option);*/

    $.ajax({
        url:"/admin/goods/update/",
        type:"POST",
        data:{
            goodsId:ugoodsId,
            goodsName:ugoodsName,
            price:uprice,
            num:unum,
            description:udescription,
            category:ucategory,
            detailCate:udetailCate,
        },
        success:function(result){
            $("#update-goods").modal('hide');
            swal(result.msg,'','success');
            to_page('/',currentPage);
        },
        error:function(){
            alert("错误！！");
        }
    });

    /*var goodsId = $("#goodsId").text();
    var updateForm = new FormData(document.getElementById("update-goods"));
    $.ajax({
        url:"//admin/goods/update/" + goodsId,
        type:"post",
        data:updateForm,
        processData:false,
        contentType:false,
        success:function(result){
            swal(result.msg,'','success');
        },
        error:function(){
            alert("错误！！");
            window.clearInterval(timer);
        }
    });*/
});

$(document).on("click",".templatemo-delete-btn",function () {
    var goodsName = $(this).parents("tr").find("td:eq(1)").text();
    var goodsId = $(this).parents("tr").find("td:eq(0)").text();
    swal({
            title: "确定删除" + goodsName + "吗？",
            type: "warning",
            showCancelButton: true,
            cancelButtonText:"取消",
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            closeOnConfirm: false,
        },
        function () {
            /*swal("删除！", "你的虚拟文件已经被删除。", "success");*/
            $.ajax({
                url: "/admin/goods/delete/" + goodsId,
                type: "DELETE",
                success:function (result) {
                    swal(result.msg, "","success");
                    to_page('/',currentPage);
                },
                error:function () {
                    to_page('/',currentPage);
                }
            });
        });
});

/*$(document).on("click",".templatemo-activity-btn",function () {
    var goodsId = $(this).parents("tr").find("td:eq(0)").text();

});*/

function showActInfo(activityId) {
    $('#activityName').text(activity[activityId-1].activityName);
    $('#activityDes').text(activity[activityId-1].activityDes);
    $('#discount').text(activity[activityId-1].discount);
    $('#fullPrice').text(activity[activityId-1].fullPrice);
    $('#reducePrice').text(activity[activityId-1].reducePrice);
    $('#fullNum').text(activity[activityId-1].fullNum);
    $('#reduceNum').text(activity[activityId-1].reduceNum);
}

$("#activity-id").change(function () {
    showActInfo($(this).val());
});

function getActivity() {
    $.ajax({
        url: "/admin/activity/showjson",
        type: "post",
        success: function (result) {
            if(result.code==100) {
                $("#activity-id").empty();
                activity = result.info.activity;
                $.each(activity, function (index,item) {
                    $("#activity-id").append($("<option></option>").attr("value",item.activityId).append(item.activityId));
                });
                showActInfo(1);
            } else {
                alert("获取活动信息失败");
            }
        }
    });
}

//保存活动信息
$(document).on("click","#saveActivity",function () {
    var goodsId = $("#activity-goodsId").text();
    var activityId = $("#activity-id").val();

    $.ajax({
        url:"/admin/activity/update/",
        type:"POST",
        data:{
            goodsId:goodsId,
            activityId:activityId
        },
        success:function(result){
            $("#activity-goods").modal('hide');
            swal(result.msg,'','success');
            to_page('/', currentPage);
        },
        error:function(){
            alert("错误！！");
        }
    });
});

function to_page(path, page) {
    $.ajax({
        url: path + "/admin/goods/showjson",
        data: "page=" + page,
        type: "get",
        success: function (result) {

            //解析显示
            build_goods_table(path, result);

            //页面信息
            build_page_info(path, result);

            //分页
            build_page_nav(path, result);

            currentPage = page;
        }
    });
}

function build_goods_table(path,result) {
    $("#goodsinfo tbody").empty();
    var goods = result.info.pageInfo.list;
    $.each(goods, function (index,item) {
        var goodsId = $("<td></td>").append(item.goodsId);
        var goodsName = $("<td></td>").append(item.goodsName);
        var price = $("<td></td>").append(item.price);
        var num = $("<td></td>").append(item.num);
        var detailCate = $("<td></td>").append(item.detailCate);
        var activityId = $("<td></td>").append(item.activityId);

        // var detailA = $('<a tabindex="0" class="btn btn-sm description" role="button" placement="top" data-toggle="popover" data-trigger="focus" title="描述" ></a>').append("描述");
        var detailBtn = $('<button type="button" class="description" data-container="body" data-toggle="popover" data-placement="top"></button>').append("描述");

        detailBtn = detailBtn.attr("data-content",item.description);

        var detailA = $("<a></a>").addClass("templatemo-link").attr("href","//detail?goodsId="+item.goodsId).append("详情");

        var editBtn = $("<button></button>").addClass("templatemo-edit-btn").append("编辑");
        var deleteBtn = $("<button></button>").addClass("templatemo-delete-btn").append("删除");

        var desTd = $("<td hidden></td>").append(detailBtn);

        //活动按钮
        var actBtn = $("<button></button>").addClass("templatemo-activity-btn").attr("data-actgoodsId",item.goodsId).append("添加");
        actBtn.click(function () {
            $("#activity-goods").modal({
                backdrop:'static'
            });
            $("#activity-goodsId").text($(this).attr("data-actgoodsId"));
            getActivity();
        });

        var actTd = $("<td></td>").append(actBtn);

        var detailTd = $("<td></td>").append(detailA);
        var editTd = $("<td></td>").append(editBtn);
        var deleteTd = $("<td></td>").append(deleteBtn);

        $("<tr></tr>").append(goodsId).append(goodsName).append(price).append(num).append(detailCate).append(activityId).append(desTd).append(detailTd).append(editTd).append(deleteTd).append(actTd).appendTo("#goodsinfo tbody");
    })
}

function build_page_info(path,result) {
    $("#page-info-area").empty();
    $("#page-info-area").append("当前第"+ result.info.pageInfo.pageNum +"页，总共"+ result.info.pageInfo.pages +"页，总共"+ result.info.pageInfo.total +"记录")
}

function build_page_nav(path,result) {
    $("#page-div-nav ul").empty();
    var pageUl = $("<ul></ul>").addClass("pagination")

    var firstPage = $("<li></li>").append($("<a aria-label=\"Next\"></a>")
        .append($("<span aria-hidden=\"true\"></span>")
            .append("首页")));

    var prePage = $("<li></li>").append($("<a aria-label=\"Next\"></a>")
        .append($("<span aria-hidden=\"true\"><i class=\"fa fa-backward\"></i></span>")));

    if(!result.info.pageInfo.hasPreviousPage) {
        prePage.addClass("li-none");
    } else {
        prePage.click(function () {
            to_page('/',result.info.pageInfo.prePage);
        });
    }

    //跳转
    firstPage.click(function () {
        to_page('/',1);
    });

    var nextPage = $("<li></li>").append($("<a aria-label=\"Next\"></a>")
        .append($("<span aria-hidden=\"true\"><i class=\"fa fa-forward\"></i></span>")));

    var lastPage = $("<li></li>").append($("<a aria-label=\"Next\"></a>")
        .append($("<span aria-hidden=\"true\"></span>")
            .append("末页")));

    if(!result.info.pageInfo.hasNextPage) {
        nextPage.addClass("li-none");
    } else {
        nextPage.click(function () {
            to_page('/',result.info.pageInfo.nextPage);
        });
    }

    lastPage.click(function () {
        to_page('/',result.info.pageInfo.lastPage);
    });

    pageUl.append(firstPage).append(prePage);

    $.each(result.info.pageInfo.navigatepageNums,function (index,item) {
        var numLi = $("<li></li>").append($("<a></a>")
            .append($("<span aria-hidden=\"true\"></span>").append(item)));
        if(result.info.pageInfo.pageNum === item) {
            numLi.addClass("active");
        }
        numLi.click(function () {
            to_page('/',item);
        });
        pageUl.append(numLi);
    });

    pageUl.append(nextPage).append(lastPage).appendTo("#page-div-nav");
}

