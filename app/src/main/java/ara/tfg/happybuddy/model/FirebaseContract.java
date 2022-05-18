package ara.tfg.happybuddy.model;


//SuperClase que contiene las equivalencias entre nombre de campos de la app
public class FirebaseContract {

    public static class UsuariosEntry {
        public static final String NODE_NAME = "usuarios";
        public static final String ID = "uid";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDOS = "apellidos";
        public static final String EMAIL = "email";
        public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String GENERO = "genero";
        public static final String NUM_TELEFONO = "num_telefono";
        public static final String PAIS = "pais";
        public static final String DIRECCION = "direccion";
        public static final String ESTADO_CIVIL = "estado_civil";
        public static final String ADMIN = "admin";
    }

    public static class CitasEntry {
        public static final String NODE_NAME = "citas";
        public static final String PACIENTE_UID = "paciente_uid";
        public static final String PROFESIONAL_UID = "profesional_uid";
        public static final String FECHA = "fecha";
    }

    public static class ProfesionalEntry {
        public static final String NODE_NAME = "profesionales";
        public static final String NOMBRE_COMPLETO = "nombre_completo";
        public static final String MASTER = "master";
        public static final String INICIO_TRABAJO = "inicio_trabajo";
        public static final String ID = "usuario_uid";
    }
}
