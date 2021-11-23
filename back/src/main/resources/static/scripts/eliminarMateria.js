function eliminar(id) {
    swal({
        title: "Esta seguro de Eliminar?",
        text: "Todos los libros no leídos vinculados a esta materia serán eliminados",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((OK) => {
            if (OK) {
                $.ajax({
                    url:"/materia/eliminar/"+id,
                    success: function(res) {
                        console.log(res);
                    },
                });
                swal("Materia eliminada", {
                    icon: "success",
                }).then((ok)=>{
                    if(ok){
                        location.href="/materia";
                    }
                });
            }
        });
}
