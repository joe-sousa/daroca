package com.example.daroca.model;

import com.example.daroca.config.ConfiguracaoAuthFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {
        private String nome;
        private String email;
        private String senha;

        private String tipo;
        private String idUsuario;
        private boolean credenciais;

        public Usuario() {
        }

        public Usuario(String email, String senha) {
            this.email = email;
            this.senha = senha;
        }

        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }

        public boolean isCredenciais() {
            return credenciais;
        }

        public void setCredenciais(boolean credenciais) {
            this.credenciais = credenciais;
        }
}


