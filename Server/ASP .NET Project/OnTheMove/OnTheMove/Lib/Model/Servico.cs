using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Servico
    {
        public Decimal Id
        {
            get;
            set;
        }

        public String Nome
        {
            get;
            set;
        }

        public String Descricao
        {
            get;
            set;
        }

        public List<Mapa> Mapas
        {
            get;
            set;
        }
    }
}