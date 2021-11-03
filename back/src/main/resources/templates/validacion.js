const pw1 = document.getElementById('contrasenia');
const pw2 = document.getElementById('confirmarContrasenia');
const boton = document.getElementById('boton');

boton.addEventListener('click', () => {
  
  if (pw1.value != pw2.value) {
    alert('Contraseña incorrecta');
  } else {
    alert('Contraseña correcta');
  }
});