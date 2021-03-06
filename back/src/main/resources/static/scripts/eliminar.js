function eliminar(id) {
    console.log(id);
    swal({
        title: "Esta seguro de Eliminar?",
        text: "Se eliminarĂ¡ permanente su cuenta y no podrĂ¡ recuperar sus datos.",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((OK) => {
            if (OK) {
                $.ajax({
                    url:"/usuario/eliminar",
                    success: function(res) {
                        console.log(res);
                    },
                });
                swal("Materia eliminada", {
                    icon: "success",
                }).then((ok)=>{
                    if(ok){
                        location.href="/logout";
                    }
                });
            }
        });
}
