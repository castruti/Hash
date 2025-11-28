# RA3 – Tabela Hash por Encadeamento Separado

Este trabalho implementa uma Tabela Hash com Encadeamento Separado em Java.  
O programa executa automaticamente todas as combinações de tamanhos de tabela, datasets e funções hash (**H_DIV**, **H_MUL**, **H_FOLD**) e gera um CSV padronizado.

---

## Como compilar

No terminal, dentro da pasta onde está `Hash.java`:

(no terminal)
```bash
javac Hash.java
```
(no arquivo csv)
```bash
java Hash > resultados.csv
```

As linhas de auditoria (H_DIV 1009 137, checksum XXXX) aparecem no stderr e não entram no CSV.

## Arquivos do repositório:

Hash.java	

resultados.csv	

README.md	

relatorio.pdf


## Funções de hashing implementadas
H_DIV → h = k % m

H_MUL → multiplicação com constante de Knuth

H_FOLD → dobramento somando blocos de 3 dígitos

## Configurações experimentais automáticas
O programa executa automaticamente todas as combinações:

✔ Tamanhos da tabela (m)
1009

10007

100003

✔ Quantidades de dados (n)
1000

10000

100000

✔ Seeds reprodutíveis
137

271828

314159

✔ Métricas coletadas
ins_ms — tempo médio de inserção

coll_tbl — colisões na tabela

coll_lst — colisões nas listas

find_ms_hits — tempo dos hits

find_ms_misses — tempo dos misses

cmp_hits — comparações de hits

cmp_misses — comparações de misses

checksum — soma dos 10 primeiros h(k) mod 1.000.003

Tudo isso é salvo automaticamente no CSV.

## Resumo da metodologia
Chaves geradas com Random(seed) (9 dígitos, reprodutível)

Estrutura: tabela hash com encadeamento separado

Inserção medindo tempos, colisões de tabela e colisões de lista

Lote de busca com 50% presentes e 50% ausentes

Medição separada de hits e misses

Repetição de cada experimento 5 vezes

Saída final em CSV padronizado



## Vídeo da apresentação
()


## Autor
Gabriel Calado da Silva Castro
