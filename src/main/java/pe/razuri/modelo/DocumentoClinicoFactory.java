/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.razuri.modelo;

/**
 *
 * @author GRLL
 */
public final class DocumentoClinicoFactory {

    private DocumentoClinicoFactory() {
    }

    public static DocumentoClinico crearDocumento(
            TipoDocumento tipo,
            int id,
            String nombreArchivo,
            String ubicacionSegura) {

        return switch (tipo) {

            case LABORATORIO ->
                    new ResultadoLaboratorio(
                            id,
                            nombreArchivo,
                            ubicacionSegura
                    );

            case EMERGENCIA ->
                    new InformeEmergencia(
                            id,
                            nombreArchivo,
                            ubicacionSegura
                    );

            case OTRO ->
                    new OtroDocumentoClinico(
                            id,
                            nombreArchivo,
                            ubicacionSegura
                    );
        };
    }
}
