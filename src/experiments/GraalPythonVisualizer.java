package experiments;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import java.io.File;
import java.util.List;

public class GraalPythonVisualizer {

    // !!! BURAYI KENDİ VENV YOLUNUZLA DEĞİŞTİRİN !!!
    // Örnek: "/Users/kullanici/my_venv" veya "C:/Proje/my_venv"
    // Proje klasörünüzde "my_venv" klasörü varsa, sadece "my_venv" yazmanız yeterli olabilir.
    private static final String VENV_PATH = "my_venv";

    /**
     * Deney 1: Parametre Ayarı için Kutu Grafiği (Boxplot) çizer.
     */
    public static void plotParameterTuning(List<Integer> popSizes, List<Double> fitnessValues) {
        String script = """
            import matplotlib.pyplot as plt
            import pandas as pd
            import seaborn as sns
            import os

            # Java verisini DataFrame'e çevir
            data = {
                'Population Size': java_pop_sizes,
                'Best Fitness': java_fitness_values
            }
            df = pd.DataFrame(data)

            # Klasör yoksa oluştur
            if not os.path.exists('data/output'):
                os.makedirs('data/output')

            # Grafik Çizimi (Boxplot)
            plt.figure(figsize=(10, 6))
            sns.boxplot(x='Population Size', y='Best Fitness', data=df, palette='viridis')
            plt.title('Experiment 1: Population Size Tuning (Boxplot)')
            plt.xlabel('Population Size')
            plt.ylabel('Best Fitness Value')
            plt.grid(True, axis='y', alpha=0.3)
            
            output_path = 'data/output/exp1_boxplot.png'
            plt.savefig(output_path)
            plt.close()
            print(f"[Python] Grafik kaydedildi: {output_path}")
            """;

        executePython(script, bindings -> {
            bindings.putMember("java_pop_sizes", popSizes);
            bindings.putMember("java_fitness_values", fitnessValues);
        });
    }

    /**
     * Deney 2: Karşılaştırma için Çubuk Grafik (Bar Chart) çizer.
     */
    public static void plotComparison(List<String> algorithms, List<Double> avgFitness, List<Double> avgTime) {
        String script = """
            import matplotlib.pyplot as plt
            import pandas as pd
            import seaborn as sns
            import os

            if not os.path.exists('data/output'):
                os.makedirs('data/output')

            # 1. Fitness Karşılaştırması
            plt.figure(figsize=(8, 5))
            sns.barplot(x=java_algos, y=java_fitness, palette='magma')
            plt.title('Experiment 2: Greedy vs GA (Average Fitness)')
            plt.ylabel('Fitness Value')
            plt.savefig('data/output/exp2_fitness_comparison.png')
            plt.close()

            # 2. Süre Karşılaştırması
            plt.figure(figsize=(8, 5))
            sns.barplot(x=java_algos, y=java_time, palette='coolwarm')
            plt.title('Experiment 2: Greedy vs GA (Average Time)')
            plt.ylabel('Time (ms)')
            plt.savefig('data/output/exp2_time_comparison.png')
            plt.close()
            
            print("[Python] Karşılaştırma grafikleri kaydedildi.")
            """;

        executePython(script, bindings -> {
            bindings.putMember("java_algos", algorithms);
            bindings.putMember("java_fitness", avgFitness);
            bindings.putMember("java_time", avgTime);
        });
    }

    /**
     * --- YENİ EKLENEN METOD ---
     * Tek bir çözümün detaylarını (hangi eşyalar seçildi, kapasite durumu vb.) görselleştirir.
     * Küçük test senaryoları ve demo (debug) işlemleri için uygundur.
     */
    public static void plotSingleSolution(double[] weights, double[] values, double capacity, boolean[] selected, String title, String filename) {
        String script = """
            import matplotlib.pyplot as plt
            import numpy as np
            import os

            if not os.path.exists('data/output'):
                os.makedirs('data/output')

            n = len(java_weights)
            indices = np.arange(n)
            
            # Renkleri belirle: Seçilenler Yeşil, Diğerleri Gri
            colors = ['#2ecc71' if s else '#bdc3c7' for s in java_selected]
            edge_colors = ['#27ae60' if s else '#95a5a6' for s in java_selected]

            # Grafik Alanı Oluştur (2 Satır: Üstte Eşyalar, Altta Kapasite)
            fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 8), gridspec_kw={'height_ratios': [3, 1]})
            fig.suptitle(java_title, fontsize=16)

            # --- PLOT 1: Eşyalar (Değer ve Ağırlık) ---
            bars = ax1.bar(indices, java_values, color=colors, edgecolor=edge_colors, linewidth=1.5)
            
            # Barların üstüne ağırlıklarını yaz
            for bar, weight in zip(bars, java_weights):
                height = bar.get_height()
                ax1.text(bar.get_x() + bar.get_width()/2., height,
                        f'W:{int(weight)}',
                        ha='center', va='bottom', fontsize=9, rotation=0)

            ax1.set_ylabel('Item Value (Profit)', fontsize=12)
            ax1.set_xlabel('Item Index', fontsize=12)
            ax1.set_xticks(indices)
            ax1.set_title('Items Visualization (Green = Selected)', fontsize=12)
            ax1.grid(True, axis='y', alpha=0.3)

            # Legend (Efsane) ekle
            from matplotlib.patches import Patch
            legend_elements = [Patch(facecolor='#2ecc71', edgecolor='#27ae60', label='Selected'),
                               Patch(facecolor='#bdc3c7', edgecolor='#95a5a6', label='Not Selected')]
            ax1.legend(handles=legend_elements)

            # --- PLOT 2: Kapasite Çubuğu ---
            total_weight = sum([w for w, s in zip(java_weights, java_selected) if s])
            
            # Kapasite çubuğu (Arkaplan - Gri)
            ax2.barh([0], [java_capacity], color='#ecf0f1', edgecolor='black', height=0.5, label='Total Capacity')
            
            # Doluluk çubuğu (Önplan - Mavi veya Kırmızı)
            # Kapasite aşıldıysa kırmızı, yoksa mavi
            fill_color = '#e74c3c' if total_weight > java_capacity else '#3498db' 
            ax2.barh([0], [total_weight], color=fill_color, height=0.5, label='Used Weight')
            
            # Metinler
            ax2.text(java_capacity/2, 0, f'Capacity: {int(java_capacity)}', ha='center', va='center', color='black', fontweight='bold')
            ax2.text(total_weight/2, 0, f'Used: {int(total_weight)}', ha='center', va='center', color='white', fontweight='bold')

            ax2.set_title('Knapsack Capacity Usage', fontsize=12)
            ax2.set_xlim(0, max(java_capacity, total_weight) * 1.1) # X eksenini genişlet
            ax2.set_yticks([]) # Y ekseni değerlerini gizle
            
            plt.tight_layout()
            
            # Kaydet
            save_path = f'data/output/{java_filename}'
            plt.savefig(save_path)
            plt.close()
            print(f"[Python] Çözüm grafiği kaydedildi: {save_path}")
            """;

        executePython(script, bindings -> {
            bindings.putMember("java_weights", weights);
            bindings.putMember("java_values", values);
            bindings.putMember("java_capacity", capacity);
            bindings.putMember("java_selected", selected);
            bindings.putMember("java_title", title);
            bindings.putMember("java_filename", filename);
        });
    }

    // GraalVM Context Oluşturma ve Çalıştırma Yardımcısı
    private static void executePython(String pythonCode, java.util.function.Consumer<Value> bindingConsumer) {
        // GraalPy çalıştırma dosyasının tam yolu
        // Windows'ta genellikle: my_venv/Scripts/graalpy.exe
        // Linux/Mac'te: my_venv/bin/graalpy
        String os = System.getProperty("os.name").toLowerCase();
        String executablePath;
        if (os.contains("win")) {
            executablePath = VENV_PATH + "/Scripts/graalpy.exe";
        } else {
            executablePath = VENV_PATH + "/bin/graalpy";
        }

        try (Context context = Context.newBuilder("python")
                .allowAllAccess(true) // Dosya sistemi erişimi için gerekli
                .option("python.Executable", executablePath)
                .option("python.ForceImportSite", "true") // Paketleri (pandas, matplotlib) tanıması için
                .build()) {

            Value bindings = context.getBindings("python");
            bindingConsumer.accept(bindings);
            context.eval("python", pythonCode);

        } catch (Exception e) {
            System.err.println("Python grafik çizimi sırasında hata oluştu. Venv kurulu mu ve yolu doğru mu?");
            e.printStackTrace();
        }
    }
}