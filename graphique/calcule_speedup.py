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

    # Calculer T(1) et Ntot(1) (référence pour le calcul du speedup)
    T1 = df[df['Threads'] == 1]['Duration'].mean()
    Ntot1 = df[df['Threads'] == 1]['Ntot'].mean()

    # Calculer le speedup faible
    df['Speedup_Faible'] = (T1 * Ntot1) / (df['Duration'] * df['Ntot'])

    # Supprimer les doublons (cas où plusieurs mesures existent pour le même N)
    df_unique = df.groupby('Threads', as_index=False).mean()

    # Tracer la courbe du Speedup faible
    plt.figure(figsize=(8, 5))
    plt.plot(df_unique['Threads'], df_unique['Speedup_Faible'], marker='o', linestyle='-', color='b', label='Speedup mesuré')

    # Tracer la courbe idéale (Speedup = 1)
    plt.axhline(y=1, linestyle='--', color='r', label='Speedup idéal (1)')

    # Ajouter des labels et une légende
    plt.xlabel("Nombre de Threads")
    plt.ylabel("Speedup")
    plt.title("Courbe du Speedup en fonction des Threads (Scalabilité Faible)")
    plt.legend()
    plt.grid()
    plt.show()


# Exemple d'utilisation

#Assignement
#plot_speedup_forte("data/Assignment102_scal_forte_G26_8c.csv")
plot_speedup_faible("data/Assignment102_scal_faible_G26_8c.csv")

#Pi
#plot_speedup_forte("data/Pi_scal_forte_G26_8c.csv")
#plot_speedup_faible("data/Pi_scal_faible_G26_8c.csv")