using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Loja
    {
        public Decimal Id
        {
            get;
            set;
        }

        public string Categoria
        {
            get;
            set;
        }

        public string Nome
        {
            get;
            set;
        }

        public string ImagemUrl
        {
            get;
            set;
        }

        public string MapaUrl
        {
            get;
            set;
        }

        public string Webmail
        {
            get;
            set;
        }

        public string Website
        {
            get;
            set;
        }

        public Decimal Telefone
        {
            get;
            set;
        }

        public string Descricao
        {
            get;
            set;
        }

        public Decimal ComPromocao
        {
            get;
            set;
        }

        public string Promocao
        {
            get;
            set;
        }
    }
}