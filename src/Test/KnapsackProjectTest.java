package Test;

import BasarOzkasli.*;
import alg.IterationBasedTC;
import alg.ga.GA;
import problem.SimpleOptimizationProblem;
import representation.SimpleSolution;
import representation.Solution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SE4492 Homework-2 - JUnit 5 Test Suite
 * Kapsam: Model, Temsil, Operatörler (Unit Tests) ve Tam GA Döngüsü (Integration Test)
 */
class KnapsackProjectTest {

    private KnapsackModel simpleModel;
    private KnapsackObjectiveFunction objFunc;
    private SimpleOptimizationProblem<KnapsackModel, KnapsackRepresentation> simpleProblem;

    @BeforeEach
    void setUp() {
        // Her testten önce temiz bir ortam oluşturalım
        // Örnek: 3 Eşya. Kapasite 10.
        // Item 0: W=5, V=10
        // Item 1: W=4, V=40
        // Item 2: W=6, V=30
        double[] weights = {5, 4, 6};
        double[] values = {10, 40, 30};
        double capacity = 10;

        simpleModel = new KnapsackModel(weights, values, capacity);
        objFunc = new KnapsackObjectiveFunction();
        simpleProblem = new SimpleOptimizationProblem<>(simpleModel, objFunc);
    }

    // --- UNIT TESTS: MODEL & REPRESENTATION ---

    @Test
    @DisplayName("Model: Feasibility (Geçerlilik) Kontrolü")
    void testIsFeasible() {
        // Durum 1: Sadece Item 0 (W=5 <= 10) -> Geçerli
        KnapsackRepresentation validRep = new KnapsackRepresentation(new boolean[]{true, false, false});
        assertTrue(simpleModel.isFeasable(validRep), "Kapasite altındaki çözüm geçerli olmalı.");

        // Durum 2: Item 0 + Item 1 (W=9 <= 10) -> Geçerli
        KnapsackRepresentation validRep2 = new KnapsackRepresentation(new boolean[]{true, true, false});
        assertTrue(simpleModel.isFeasable(validRep2), "Tam sınıra yakın çözüm geçerli olmalı.");

        // Durum 3: Item 0 + Item 2 (W=11 > 10) -> Geçersiz
        KnapsackRepresentation invalidRep = new KnapsackRepresentation(new boolean[]{true, false, true});
        assertFalse(simpleModel.isFeasable(invalidRep), "Kapasiteyi aşan çözüm geçersiz olmalı.");
    }

    @Test
    @DisplayName("Objective Function: Değer Hesaplama")
    void testObjectiveValue() {
        // Item 1 (40) + Item 2 (30) = 70
        // (Not: Model feasible değilse bile objective saf değeri hesaplar,
        // feasibility kontrolü modelin veya algoritmanın sorumluluğundadır)
        KnapsackRepresentation rep = new KnapsackRepresentation(new boolean[]{false, true, true});
        double val = objFunc.value(simpleModel, rep);

        assertEquals(70.0, val, 0.0001, "Seçilen eşyaların toplam değeri doğru hesaplanmalı.");
    }

    // --- UNIT TESTS: OPERATORS (ISG, MUTATION, CROSSOVER) ---

    @Test
    @DisplayName("ISG: Rastgele Çözüm Üretimi ve Onarım")
    void testRandomISG() {
        KnapsackRandomISG isg = new KnapsackRandomISG();

        // Çok sayıda üretim yapıp hepsinin feasible olduğunu doğrulayalım
        for (int i = 0; i < 50; i++) {
            Solution s = isg.generate(simpleProblem);
            KnapsackRepresentation rep = (KnapsackRepresentation) s.getRepresentation();

            assertTrue(simpleModel.isFeasable(rep), "ISG tarafından üretilen her çözüm kapasite sınırında (feasible) olmalı.");
        }
    }

    @Test
    @DisplayName("Mutation: Değişim ve Onarım")
    void testMutation() {
        KnapsackMutation mutation = new KnapsackMutation();

        // Başlangıç: Item 0 ve Item 2 seçili (W=11, Geçersiz).
        // Ancak biz bunu geçerli bir solution objesi gibi veriyoruz, mutation bunu düzeltmeli.
        boolean[] items = {true, false, true};
        KnapsackRepresentation rep = new KnapsackRepresentation(items);
        Solution s = new SimpleSolution(rep, 0); // Değer önemsiz

        // Mutation'ı zorla çalıştırıp repair mekanizmasını test edelim
        // Not: Mutation olasılıksal olduğu için kesin değişim garantisi zordur ama
        // repair her halükarda çalışmalıdır.
        mutation.apply(simpleProblem, s);

        KnapsackRepresentation mutatedRep = (KnapsackRepresentation) s.getRepresentation();
        assertTrue(simpleModel.isFeasable(mutatedRep), "Mutasyon sonrası çözüm mutlaka feasible hale getirilmeli (Repair çalışmalı).");
    }

    @Test
    @DisplayName("Crossover: Çocuk Üretimi ve Onarım")
    void testCrossover() {
        KnapsackCrossover crossover = new KnapsackCrossover();

        // Ebeveyn 1: Item 0, Item 1 (W=9, V=50) -> Feasible
        boolean[] p1Genes = {true, true, false};
        Solution p1 = new SimpleSolution(new KnapsackRepresentation(p1Genes), 50);

        // Ebeveyn 2: Item 2 (W=6, V=30) -> Feasible
        boolean[] p2Genes = {false, false, true};
        Solution p2 = new SimpleSolution(new KnapsackRepresentation(p2Genes), 30);

        List<Solution> offspring = crossover.apply(simpleProblem, p1, p2);

        assertNotNull(offspring, "Crossover null dönmemeli.");
        assertEquals(2, offspring.size(), "Crossover 2 çocuk üretmeli.");

        for (Solution child : offspring) {
            KnapsackRepresentation childRep = (KnapsackRepresentation) child.getRepresentation();
            assertTrue(simpleModel.isFeasable(childRep), "Çocuklar onarılmış ve feasible olmalı.");
        }
    }

    // --- UNIT TESTS: SELECTORS ---

    @Test
    @DisplayName("Parent Selector: Seçim Adedi")
    void testParentSelector() {
        KnapsackParentSelector selector = new KnapsackParentSelector();
        List<Solution> population = new ArrayList<>();

        // Popülasyonu rastgele doldur
        for(int i=0; i<10; i++) {
            population.add(new SimpleSolution(new KnapsackRepresentation(new boolean[3]), i));
        }

        List<Solution> parents = selector.select(simpleProblem, population);

        assertNotNull(parents);
        assertEquals(2, parents.size(), "Steady-State GA için tam olarak 2 ebeveyn seçilmeli.");
    }

    @Test
    @DisplayName("Victim Selector: En Kötüleri Seçme")
    void testVictimSelector() {
        KnapsackVictimSelector selector = new KnapsackVictimSelector();
        List<Solution> population = new ArrayList<>();

        // Popülasyon: Değerleri 10, 20, 30, 40
        population.add(new SimpleSolution(new KnapsackRepresentation(new boolean[3]), 10)); // En kötü
        population.add(new SimpleSolution(new KnapsackRepresentation(new boolean[3]), 40));
        population.add(new SimpleSolution(new KnapsackRepresentation(new boolean[3]), 20)); // İkinci en kötü
        population.add(new SimpleSolution(new KnapsackRepresentation(new boolean[3]), 30));

        // 2 kurban seç
        List<Solution> victims = selector.select(simpleProblem, population, 2);

        assertEquals(2, victims.size());
        // En kötülerin (10 ve 20) seçildiğini doğrula
        double val1 = victims.get(0).value();
        double val2 = victims.get(1).value();

        assertTrue((val1 == 10 && val2 == 20) || (val1 == 20 && val2 == 10),
                "Fitness değeri en düşük olan bireyler kurban seçilmeli.");
    }

    // --- INTEGRATION TEST: FULL GA ---

    @Test
    @DisplayName("Integration: Tam GA Döngüsü (Optimum Bulma)")
    void testFullGAIntegration() {
        // Biraz daha karmaşık bir problem
        // Kapasite 15. Optimum 45 (Item 0, 1, 3)
        double[] w = {2, 5, 8, 3, 1};
        double[] v = {10, 20, 30, 15, 5};
        KnapsackModel integModel = new KnapsackModel(w, v, 15);
        SimpleOptimizationProblem<KnapsackModel, KnapsackRepresentation> problem =
                new SimpleOptimizationProblem<>(integModel, new KnapsackObjectiveFunction());

        // GA Konfigürasyonu
        GA<KnapsackModel, KnapsackRepresentation> ga = new GA<>(new IterationBasedTC<>(200));
        ga.setPopulationSize(20);
        ga.setCrossOverRate(0.9);
        ga.setMutationRate(1.0); // Operatör içi olasılık
        ga.setInitialSolutionGenerator(new KnapsackRandomISG());
        ga.setCrossOverOperator(new KnapsackCrossover());
        ga.setMutationOperator(new KnapsackMutation());
        ga.setParentSelector(new KnapsackParentSelector());
        ga.setVictimSelector(new KnapsackVictimSelector());

        // Çalıştır
        Solution best = ga.solve(problem);

        // Assertions
        assertNotNull(best, "GA bir çözüm döndürmeli.");
        assertTrue(integModel.isFeasable((KnapsackRepresentation) best.getRepresentation()), "En iyi çözüm feasible olmalı.");

        // Optimum değer 45 civarı olmalı (GA stokastik olduğu için >= 40 diyebiliriz)
        // Item 0(10)+1(20)+3(15) = 45. W=2+5+3=10 <= 15.
        // Veya Item 2(30)+1(20) = 50 (W=13 <= 15). (Pardon, Item 2 8kg, Item 1 5kg. 8+5=13. 30+20=50).
        // Demek ki optimum 50.
        assertTrue(best.value() >= 45.0, "GA makul bir çözüm (>= 45) bulmalı. Bulunan: " + best.value());

        System.out.println("Integration Test Başarılı. Bulunan Değer: " + best.value());
    }
}