# goldfinder 


Jeu multijoueur de collecte de trésors dans un labyrinthe avec une architecture 
client-serveur

# Rapport de Projet : Jeu Labyrinthe en JavaFX

## Objectif du Projet
L'objectif de ce projet est de développer un jeu de labyrinthe multijoueur simple avec une architecture client-serveur. Le jeu permet à un joueur d'explorer un labyrinthe généré aléatoirement, de collecter des pièces d'or et de naviguer à travers le labyrinthe en utilisant les commandes clavier.

## Architecture
- **Serveur (`AppServer`)**: Gère la logique du jeu, y compris la génération du labyrinthe, le traitement des mouvements des joueurs, et la vérification des conditions de fin de jeu.
- **Client (`AppClient`)**: Fournit une interface utilisateur pour interagir avec le jeu, affiche l'état actuel du labyrinthe et transmet les commandes du joueur au serveur.

## Implémentation

### Serveur (`AppServer`)
- **Génération du Labyrinthe**: Utilise la classe `Grid` pour générer un labyrinthe basé sur une grille rectangulaire avec des murs et des pièces d'or placées aléatoirement.
- **Traitement des Commandes**: Écoute les commandes du joueur (mouvements et autres commandes de contrôle) et met à jour l'état du jeu en conséquence.
- **Fin de Jeu**: Vérifie si toutes les pièces d'or ont été collectées pour déterminer si le jeu est terminé.

### Client (`AppClient`)
- **Affichage**: Utilise JavaFX pour afficher l'état actuel du labyrinthe, y compris la position du joueur, les murs et les pièces d'or.
- **Entrées Utilisateur**: Capture les entrées clavier pour le déplacement du joueur et envoie ces commandes au serveur.
- **Contrôles de Jeu**: Propose des boutons pour démarrer, mettre en pause, avancer d'un pas, et redémarrer le jeu.

## Problèmes rencontrés

### Capture des Commandes Clavier
Le principal problème rencontré durant le développement concerne la capture des commandes clavier dans l'interface utilisateur. Malgré la mise en place d'un système d'écoute des entrées clavier, les commandes ne sont pas correctement capturées ou transmises au serveur, ce qui empêche le joueur de déplacer le personnage dans le labyrinthe comme prévu.

## Possibles Solutions
- **Assurer le Focus**: S'assurer que le composant JavaFX qui est censé capturer les entrées clavier a le focus.
- **Révision du Système d'Événements**: Vérifier l'implémentation des événements clavier pour s'assurer qu'ils sont correctement enregistrés et traités.

## Conclusion
Le projet a réussi à mettre en place une architecture client-serveur pour un jeu de labyrinthe et à implémenter la majorité des fonctionnalités de base. Cependant, le problème de capture des commandes clavier nécessite une révision supplémentaire pour atteindre une fonctionnalité complète. Une fois ce problème résolu, le jeu devrait offrir une expérience interactive permettant aux joueurs de naviguer dans un labyrinthe généré aléatoirement et de collecter des pièces d'or.

## Prochaines Étapes
- **Résoudre le Problème de Capture des Commandes Clavier**: Afin de rendre le jeu pleinement fonctionnel, il est crucial de résoudre le problème de capture des commandes clavier.
- **Ajouter des Fonctionnalités Multijoueur**: Explorer la possibilité d'ajouter un support multijoueur complet, permettant à plusieurs joueurs de participer simultanément.

