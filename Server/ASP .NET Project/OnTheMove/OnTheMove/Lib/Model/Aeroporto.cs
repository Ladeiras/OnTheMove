using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Aeroporto
    {
        public Decimal Id
        {
            get;
            set;
        }

        public string Pais
        {
            get;
            set;
        }

        public string Cidade
        {
            get;
            set;
        }

        public string Nome
        {
            get;
            set;
        }

        public double Latitude
        {
            get;
            set;
        }

        public double Longitude
        {
            get;
            set;
        }
    }
}