/*
    RECUPERAÇÃO RA3 - VERSÃO FINAL CORRIGIDA
    Tabela Hash por Encadeamento Separado (implementação simples em Java básico).
*/

import java.util.Random;

public class Hash {

    public static final int REPETICOES = 5;
    public static final int TAM_LOTE_PADRAO = 1000;

    public static class Registro {
        public int chave;
        public Registro(int c) { this.chave = c; }
    }

    public static class No {
        public Registro reg;
        public No prox;
        public No(Registro r) { this.reg = r; this.prox = null; }
    }

    public static class TabelaHash {
        public int M;
        public No[] tabela;

        public long colisaoTabela;
        public long colisaoLista;
        public long comparacoesHits;
        public long comparacoesMisses;

        public TabelaHash(int m) {
            this.M = m;
            this.tabela = new No[m];

            int i = 0;
            while (i < m) {
                tabela[i] = null;
                i = i + 1;
            }

            this.colisaoTabela = 0;
            this.colisaoLista = 0;
            this.comparacoesHits = 0;
            this.comparacoesMisses = 0;
        }

        public void inserir(Registro r, int h) {
            No inicio = tabela[h];

            if (inicio == null) {
                tabela[h] = new No(r);
            } else {
                colisaoTabela = colisaoTabela + 1;

                No atual = inicio;
                long passos = 0;

                while (atual.prox != null) {
                    atual = atual.prox;
                    passos = passos + 1;
                }

                colisaoLista = colisaoLista + passos;
                atual.prox = new No(r);
            }
        }

        public int buscar(int chave, int h) {
            No atual = tabela[h];

            if (atual == null) {
                comparacoesMisses = comparacoesMisses + 0;
                return 0;
            }

            int comps = 0;

            while (atual != null) {
                comps = comps + 1;

                if (atual.reg.chave == chave) {
                    comparacoesHits = comparacoesHits + comps;
                    colisaoLista = colisaoLista + (comps - 1);
                    return comps;
                }

                atual = atual.prox;
            }

            comparacoesMisses = comparacoesMisses + comps;
            colisaoLista = colisaoLista + comps;
            return -comps;
        }
    }

    public static int hDiv(int k, int m) {
        int hv = k % m;
        if (hv < 0) hv = hv + m;
        return hv;
    }

    public static int hMul(int k, int m) {
        long A = 2654435761L;
        long x = (long) k * A;
        int hv = (int) ((x >>> 28) % m);
        if (hv < 0) hv = hv + m;
        return hv;
    }

    public static int hFold(int k, int m) {
        int soma = 0;
        int tmp = k;

        while (tmp > 0) {
            soma = soma + (tmp % 1000);
            tmp = tmp / 1000;
        }

        int hv = soma % m;
        if (hv < 0) hv = hv + m;
        return hv;
    }

    public static int calcularHash(int k, String func, int m) {
        if (func.equals("H_DIV")) {
            return hDiv(k, m);
        } else {
            if (func.equals("H_MUL")) {
                return hMul(k, m);
            } else {
                return hFold(k, m);
            }
        }
    }

    public static int[] gerar(int n, long seed) {
        int[] d = new int[n];
        Random r = new Random(seed);

        int i = 0;
        while (i < n) {
            d[i] = r.nextInt(900000000) + 100000000;
            i = i + 1;
        }

        return d;
    }

    public static void embaralhar(int[] v, int tam, long seed) {
        Random r = new Random(seed);

        int i = tam - 1;
        while (i > 0) {
            int j = r.nextInt(i + 1);

            int aux = v[i];
            v[i] = v[j];
            v[j] = aux;

            i = i - 1;
        }
    }

    public static int[] criarLote(int[] dados, int tam, long seed, int nDados) {
        int[] lote = new int[tam];

        int metade = tam / 2;
        int i = 0;

        while (i < metade) {
            lote[i] = dados[i];
            i = i + 1;
        }

        Random r = new Random(seed + 999);
        int pos = metade;

        while (pos < tam) {
            int cand = r.nextInt(900000000) + 100000000;

            boolean existe = false;
            int j = 0;

            while (j < nDados) {
                if (dados[j] == cand) {
                    existe = true;
                    j = nDados;
                } else {
                    j = j + 1;
                }
            }

            if (!existe) {
                lote[pos] = cand;
                pos = pos + 1;
            }
        }

        embaralhar(lote, tam, seed + 5555);

        return lote;
    }

    public static void executar(int m, int n, String func, long seed) {

        System.err.println(func + " " + m + " " + seed);

        int[] dados = gerar(n, seed);

        int soma10 = 0;
        int cidx = 0;

        while (cidx < 10 && cidx < n) {
            int k = dados[cidx];
            int h = calcularHash(k, func, m);
            soma10 = soma10 + h;
            cidx = cidx + 1;
        }

        int checksum = soma10 % 1000003;
        if (checksum < 0) checksum = checksum + 1000003;

        {
            TabelaHash thw = new TabelaHash(m);
            int wi = 0;

            while (wi < n) {
                int wk = dados[wi];
                int wh = calcularHash(wk, func, m);
                thw.inserir(new Registro(wk), wh);
                wi = wi + 1;
            }
        }

        long somaIns = 0;
        long somaColTbl = 0;
        long somaColLst = 0;

        int reps = 0;

        while (reps < REPETICOES) {

            TabelaHash th = new TabelaHash(m);

            long ini = System.nanoTime();

            int i = 0;
            while (i < n) {
                int k = dados[i];
                int h = calcularHash(k, func, m);
                th.inserir(new Registro(k), h);
                i = i + 1;
            }

            long fim = System.nanoTime();
            long tempoMs = (fim - ini) / 1000000L;

            somaIns = somaIns + tempoMs;
            somaColTbl = somaColTbl + th.colisaoTabela;
            somaColLst = somaColLst + th.colisaoLista;

            reps = reps + 1;
        }

        long insMed = somaIns / REPETICOES;
        long colTblMed = somaColTbl / REPETICOES;
        long colLstMedIns = somaColLst / REPETICOES;

        System.err.println("checksum " + checksum);

        int tamLote = TAM_LOTE_PADRAO;
        if (tamLote > n) tamLote = n;

        int[] lote = criarLote(dados, tamLote, seed, n);

        {
            TabelaHash thw2 = new TabelaHash(m);

            int ii = 0;
            while (ii < n) {
                int k2 = dados[ii];
                int h2 = calcularHash(k2, func, m);
                thw2.inserir(new Registro(k2), h2);
                ii = ii + 1;
            }

            int jj = 0;
            while (jj < tamLote) {
                int v = lote[jj];
                int hh = calcularHash(v, func, m);
                thw2.buscar(v, hh);
                jj = jj + 1;
            }
        }

        long somaHitMs = 0;
        long somaMissMs = 0;
        long somaHitCmp = 0;
        long somaMissCmp = 0;

        reps = 0;

        while (reps < REPETICOES) {

            TabelaHash th = new TabelaHash(m);

            int i = 0;
            while (i < n) {
                int k = dados[i];
                int h = calcularHash(k, func, m);
                th.inserir(new Registro(k), h);
                i = i + 1;
            }

            th.comparacoesHits = 0;
            th.comparacoesMisses = 0;

            long tHits = 0;
            long tMiss = 0;

            int j = 0;
            while (j < tamLote) {
                int valor = lote[j];
                int h = calcularHash(valor, func, m);

                long t0 = System.nanoTime();
                int r = th.buscar(valor, h);
                long t1 = System.nanoTime();

                long delta = (t1 - t0) / 1000000L;

                if (r > 0) {
                    tHits = tHits + delta;
                } else {
                    tMiss = tMiss + delta;
                }

                j = j + 1;
            }

            somaHitMs = somaHitMs + tHits;
            somaMissMs = somaMissMs + tMiss;
            somaHitCmp = somaHitCmp + th.comparacoesHits;
            somaMissCmp = somaMissCmp + th.comparacoesMisses;

            reps = reps + 1;
        }

        long hitMed = somaHitMs / REPETICOES;
        long missMed = somaMissMs / REPETICOES;
        long cmpHitMed = somaHitCmp / REPETICOES;
        long cmpMissMed = somaMissCmp / REPETICOES;

        long colLstMed = colLstMedIns;

        System.out.println(
            m + "," + n + "," + func + "," + seed + "," +
            insMed + "," + colTblMed + "," + colLstMed + "," +
            hitMed + "," + missMed + "," + cmpHitMed + "," +
            cmpMissMed + "," + checksum
        );
    }

    public static void main(String[] args) {

        System.out.println("m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum");

        int[] Ms = {1009, 10007, 100003};
        int[] Ns = {1000, 10000, 100000};
        String[] Fs = {"H_DIV", "H_MUL", "H_FOLD"};
        long[] Ss = {137L, 271828L, 314159L};

        int i = 0;
        while (i < 3) {
            int m = Ms[i];

            int j = 0;
            while (j < 3) {
                int n = Ns[j];

                int k = 0;
                while (k < 3) {
                    String f = Fs[k];

                    int s = 0;
                    while (s < 3) {
                        executar(m, n, f, Ss[s]);
                        s = s + 1;
                    }

                    k = k + 1;
                }

                j = j + 1;
            }

            i = i + 1;
        }
    }
}
