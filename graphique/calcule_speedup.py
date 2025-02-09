import pandas as pd
import matplotlib.pyplot as plt

def plot_speedup_forte(csv_file):
    # Charger les données du CSV
    df = pd.read_csv(csv_file)

    # Obtenir toutes les valeurs uniques de Ntot
    ntots = df['Ntot'].unique()

    # Créer une figure pour les courbes
    plt.figure(figsize=(8, 5))

    # Boucle pour tracer une courbe pour chaque valeur unique de Ntot
    for n in ntots:
        df_subset = df[df['Ntot'] == n]

        # Grouper par 'Threads' et calculer la moyenne des 'Duration'
        df_avg = df_subset.groupby('Threads')['Duration'].mean().reset_index()

        # Extraire les valeurs utiles
        threads = df_avg['Threads']
        avg_durations = df_avg['Duration']

        # Calculer le speedup
        t1 = avg_durations.iloc[0]  # Temps d'exécution pour 1 thread
        speedup = t1 / avg_durations

        # Tracer la courbe pour cette valeur de Ntot
        plt.plot(threads, speedup, marker='o', linestyle='-', label=f'Speedup Ntot = {int(n)}')

    # Ajouter la courbe de speedup idéal
    plt.plot(threads, threads, linestyle='--', color='r', label='Speedup idéal')

    # Ajouter des labels et une légende
    plt.xlabel("Nombre de Threads")
    plt.ylabel("Speedup")
    plt.title("Courbe du Speedup en fonction des Threads")
    plt.legend()
    plt.grid()
    plt.show()

def plot_speedup_faible(csv_file):
    # Charger les données du CSV
    df = pd.read_csv(csv_file)

    # Extraire les valeurs utiles
    threads = df['Threads']
    durations = df['Duration']
    ntots = df['Ntot']

    # Calculer la charge totale
    total_work = ntots.iloc[0]  # Charge de travail de la première ligne (en théorie, identique pour toutes les lignes)

    # Calculer le speedup pour la scalabilité faible
    speedup_faible = durations.iloc[0] * threads / total_work

    # Tracer la courbe du speedup
    plt.figure(figsize=(8, 5))
    plt.plot(threads, speedup_faible, marker='o', linestyle='-', color='b', label='Speedup mesuré')

    # Tracer la courbe idéale de speedup (devrait être proche de 1)
    plt.plot(threads, [1] * len(threads), linestyle='--', color='r', label='Speedup idéal')

    # Ajouter des labels et une légende
    plt.xlabel("Nombre de Threads")
    plt.ylabel("Speedup")
    plt.title("Courbe du Speedup en fonction des Threads (Scalabilité Faible)")
    plt.legend()
    plt.grid()
    plt.show()

# Exemple d'utilisation
#plot_speedup_forte("data/Pi_scal_forte_G21_12c.csv")
plot_speedup_faible("data/Pi_scal_faible_G21_12c.csv")