package GestorEjercicios.ejemplo;

import GestorEjercicios.integracion.IntegracionGamificacion;
import java.util.*;

/**
 * 🧪 EJEMPLO DE USO DE LA INTEGRACIÓN CON GAMIFICACIÓN
 * 
 * Esta clase demuestra cómo el módulo de Gamificación puede usar
 * la IntegracionGamificacion para obtener datos de progreso.
 */
public class EjemploIntegracionGamificacion {
    
    public static void main(String[] args) {
        System.out.println("🎮 === EJEMPLO DE INTEGRACIÓN CON GAMIFICACIÓN ===\n");
        
        // 1. Obtener usuarios activos
        demostrarUsuariosActivos();
        
        // 2. Mostrar estadísticas individuales
        demostrarEstadisticasUsuario();
        
        // 3. Simular verificación de desafíos
        demostrarVerificacionDesafios();
        
        // 4. Simular ranking semanal
        demostrarRankingSemanal();
    }
    
    private static void demostrarUsuariosActivos() {
        System.out.println("👥 === USUARIOS ACTIVOS ===");
        List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
        
        if (usuarios.isEmpty()) {
            System.out.println("   ⚠️ No hay usuarios activos aún.");
            System.out.println("   💡 Completa algunas lecciones primero para ver datos.\n");
            return;
        }
        
        for (String usuario : usuarios) {
            System.out.println("   👤 " + usuario);
        }
        System.out.println("   Total: " + usuarios.size() + " usuarios\n");
    }
    
    private static void demostrarEstadisticasUsuario() {
        System.out.println("📊 === ESTADÍSTICAS POR USUARIO ===");
        List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
        
        if (usuarios.isEmpty()) {
            System.out.println("   ⚠️ No hay datos para mostrar.\n");
            return;
        }
        
        for (String usuario : usuarios) {
            System.out.println("📋 Usuario: " + usuario);
            System.out.println("   💎 XP Total: " + IntegracionGamificacion.getXPTotal(usuario));
            System.out.println("   🧠 Conocimiento Total: " + IntegracionGamificacion.getConocimientoTotal(usuario));
            System.out.println("   📚 Lecciones Totales: " + IntegracionGamificacion.getLeccionesTotales(usuario));
            System.out.println("   📅 Lecciones Esta Semana: " + IntegracionGamificacion.getLeccionesSemanaActual(usuario));
            System.out.println("   📆 Lecciones Este Mes: " + IntegracionGamificacion.getLeccionesMesActual(usuario));
            System.out.println();
        }
    }
    
    private static void demostrarVerificacionDesafios() {
        System.out.println("🎯 === SIMULACIÓN DE DESAFÍOS ===");
        List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
        
        if (usuarios.isEmpty()) {
            System.out.println("   ⚠️ No hay usuarios para verificar desafíos.\n");
            return;
        }
        
        // Definir metas de ejemplo
        int metaSemanal = 5;
        int metaMensual = 20;
        
        System.out.println("   🎯 Meta Semanal: " + metaSemanal + " lecciones");
        System.out.println("   🎯 Meta Mensual: " + metaMensual + " lecciones\n");
        
        for (String usuario : usuarios) {
            boolean cumplioSemanal = IntegracionGamificacion.cumplioMetaSemanal(usuario, metaSemanal);
            boolean cumplioMensual = IntegracionGamificacion.cumplioMetaMensual(usuario, metaMensual);
            
            System.out.println("   👤 " + usuario + ":");
            System.out.println("      📅 Desafío Semanal: " + (cumplioSemanal ? "✅ COMPLETADO" : "❌ En progreso"));
            System.out.println("      📆 Desafío Mensual: " + (cumplioMensual ? "✅ COMPLETADO" : "❌ En progreso"));
            System.out.println();
        }
    }
    
    private static void demostrarRankingSemanal() {
        System.out.println("🏆 === RANKING SEMANAL ===");
        List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
        
        if (usuarios.isEmpty()) {
            System.out.println("   ⚠️ No hay datos para el ranking.\n");
            return;
        }
        
        // Crear lista de usuarios con sus lecciones semanales
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>();
        for (String usuario : usuarios) {
            int lecciones = IntegracionGamificacion.getLeccionesSemanaActual(usuario);
            ranking.add(new AbstractMap.SimpleEntry<>(usuario, lecciones));
        }
        
        // Ordenar por lecciones completadas (descendente)
        ranking.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));
        
        // Mostrar ranking
        for (int i = 0; i < ranking.size(); i++) {
            Map.Entry<String, Integer> entry = ranking.get(i);
            String medalla = "";
            switch (i) {
                case 0: medalla = "🥇"; break;
                case 1: medalla = "🥈"; break;
                case 2: medalla = "🥉"; break;
                default: medalla = "   ";
            }
            
            System.out.println("   " + medalla + " " + (i + 1) + ". " + 
                             entry.getKey() + " - " + entry.getValue() + " lecciones");
        }
        System.out.println();
    }
    
    /**
     * 🔧 Método de utilidad para mostrar reporte completo de un usuario
     */
    public static void mostrarReporteUsuario(String usuario) {
        System.out.println(IntegracionGamificacion.getReporteUsuario(usuario));
    }
    
    /**
     * 🎮 Simulación de cómo Gamificación podría verificar desafíos automáticamente
     */
    public static void verificarDesafiosAutomaticos() {
        System.out.println("🤖 === VERIFICACIÓN AUTOMÁTICA DE DESAFÍOS ===");
        
        List<String> usuarios = IntegracionGamificacion.getUsuariosActivos();
        
        for (String usuario : usuarios) {
            Map<String, Integer> stats = IntegracionGamificacion.getEstadisticasCompletas(usuario);
            
            // Verificar si merece logros automáticos
            if (stats.get("lecciones_ultima_semana") >= 5) {
                System.out.println("🏆 " + usuario + " merece el logro 'Estudiante Dedicado' (5+ lecciones/semana)");
            }
            
            if (stats.get("lecciones_ultimo_mes") >= 20) {
                System.out.println("🏆 " + usuario + " merece el logro 'Estudiante Constante' (20+ lecciones/mes)");
            }
            
            if (stats.get("xp_total") >= 1000) {
                System.out.println("🏆 " + usuario + " merece el logro 'Acumulador de XP' (1000+ XP total)");
            }
        }
    }
}
