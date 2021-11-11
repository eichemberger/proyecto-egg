const pw1 = document.getElementById('contrasenia');
const pw2 = document.getElementById('confirmar-contrasenia')
const boton = document.getElementById('boton')
const msgError = document.getElementById('error-login');


pw2.addEventListener('input', () => {
    if (pw1.value !== pw2.value) {
        boton.disabled = true;
        msgError.style.display = 'contents';

    } else {
        boton.disabled = false;
        msgError.style.display = 'none';

    }
});
