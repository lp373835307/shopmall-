/**
 * Created by 文辉 on 2017/7/25.
 */
$(document).ready(function () {
    $("#confirm-orders").click(function () {
        // alert("safd");
        var selectAddr =  $('input:radio[name="addressId"]:checked').val();
        if(selectAddr == null) {
            swal("请先添加地址");
            return;
        }
        var isPay = $('#pay-select').val();
        var oldPrice = $('#total-old').text();
        var newPrice = $('#total-new').text();
        $.ajax({
            url: "/shop/orderFinish",
            type: "POST",
            data: {
                oldPrice: oldPrice,
                newPrice: newPrice,
                isPay: isPay,
                addressId: selectAddr
            },
            success: function () {
                swal("购买成功", "", "success");
                location.href = "/shop/info/list"
            },
            error: function () {
                swal("购买失败，无法连接到服务器！");
            }
        });
    });
});