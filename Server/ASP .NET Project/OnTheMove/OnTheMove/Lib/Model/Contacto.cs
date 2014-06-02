using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Contacto
    {
        public Decimal Id
        {
            get;
            set;
        }

        public Decimal IdAeroporto
        {
            get;
            set;
        }

        public string Nome
        {
            get;
            set;
        }

        public string Titulo
        {
            get;
            set;
        }

        public string Website
        {
            get;
            set;
        }

        public string MapaURL
        {
            get;
            set;
        }

        public string Descricao
        {
            get;
            set;
        }

        public List<Decimal> Telefones
        {
            get;
            set;
        }
    }
}