function registerUser(){

    let user={

        name:document.getElementById("name").value,
        email:document.getElementById("email").value,
        phone:document.getElementById("phone").value,
        password:document.getElementById("password").value
    };

    fetch("http://localhost:8080/api/register",{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify(user)

    })

    .then(response=>response.json())

    .then(data=>{

        document.getElementById("msg").innerHTML=
        "Registration Successful. OTP Sent.";

        setTimeout(function(){

            window.location.href="otp.html";

        },1500);

    })

    .catch(error=>{

        alert("Registration Failed");

    });

}



function verifyOtp(){

    let email=document.getElementById("otpEmail").value;
    let otp=document.getElementById("otp").value;

    fetch("http://localhost:8080/api/verify-email?email="+email+"&otp="+otp,{

        method:"POST"

    })

    .then(response=>response.text())

    .then(data=>{

        document.getElementById("otpMsg").innerHTML=data;

        if(data.includes("Successfully")){

            setTimeout(function(){

                window.location.href="login.html";

            },1500);
        }

    });

}



function loginUser(){

    let user={

        email:document.getElementById("loginEmail").value,

        password:document.getElementById("loginPassword").value
    };

    fetch("http://localhost:8080/api/login",{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify(user)

    })

    .then(response=>response.text())

    .then(data=>{

        document.getElementById("loginMsg").innerHTML=data;

        if(data.includes("Successful")){

            setTimeout(function(){

                window.location.href="dashboard.html";

            },1500);

        }

    });

}