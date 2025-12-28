#!/bin/bash

# Créer le dossier crypto s'il n'existe pas
mkdir -p crypto

# Liste des éléments à exclure (ne pas déplacer)
exclude_list=(".git" ".gitignore" "crypto" "bin" "move_to_crypto.sh" ".devcontainer" ".github" "README.md" "LICENSE")

# Fonction pour vérifier si un élément est dans la liste d'exclusion
is_excluded() {
    local item="$1"
    for excl in "${exclude_list[@]}"; do
        if [[ "$item" == "$excl" ]]; then
            return 0
        fi
    done
    return 1
}

# Déplacer les éléments
for item in * .[^.]*; do
    if [[ "$item" != "." && "$item" != ".." ]]; then
        if ! is_excluded "$item"; then
            if [[ -e "$item" ]]; then
                echo "Déplacement de: $item"
                # Utiliser git mv si suivi par git, sinon mv
                git mv "$item" crypto/ 2>/dev/null || mv "$item" crypto/
            fi
        fi
    fi
done