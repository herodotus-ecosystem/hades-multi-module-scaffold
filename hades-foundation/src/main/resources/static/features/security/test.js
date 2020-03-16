var tools = new SecurityTools();
function hello() {
    alert('hello world');
    debugger;
    var timestamp = Number(new Date()) ;
    var key="95f50ff8-36b3-4409-b496-a99efc068de4";
    console.log(timestamp);
    console.log(key);
    console.log(tools.md5(timestamp + "#" + key));
    //?timestamp=1564560997345&token=c16d7c5ce0433561722735f5afb29981
}

function test(abc) {
    alert(abc);
}

$(function () {
    $('#btnabc').on('click', function () {
        hello();
        test($('#renameInput').val());
        var orig = $('#renameInput').val();
        var base64String = '';


        console.log("原值:" + orig)
        // $.base64.encode($('#personkit').val()),
        base64String = tools.Base64Encrypt(orig);
        console.log("base64加密:" + base64String);
        console.log("base64解密:" + tools.Base64Decrypt(base64String));

        var base64AndAesString = '';

        base64AndAesString = tools.AesEncrypt(base64String);
        // base64AndAesString=tools.AesEncrypt(orig);
        console.log(" AES加密:" + base64AndAesString);
        console.log(" AES解密:" + tools.AesDecrypt(base64AndAesString));


        var aesString = '';

        aesString = tools.AesEncrypt("18314148520");
        console.log(" AES加密:" + aesString);
        console.log(" AES解密:" + tools.AesDecrypt(aesString));
        console.log(" context_path:" + context_path);

// $.base64.encode($('#personkit').val()),
        $.post(context_path + '/management/test', {
            base64AndAesString: base64AndAesString,
            aesString: aesString
        }, function (data) {
            console.log(" server say :" + data.msg);
            var obj = JSON.parse(data.msg);
            console.log(" server say a:" + obj.a);
            console.log("a AES解密:" + tools.AesDecrypt(obj.a));
            console.log("base64解密:" + tools.Base64Decrypt(tools.AesDecrypt(obj.a)));
            console.log(" server say b:" + obj.b);
            console.log("b AES解密:" + tools.AesDecrypt(obj.b));
        }, 'json');

    });
});
