using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Mapa
    {
        public Decimal Id
        {
            get;
            set;
        }

        public String Localizacao
        {
            get;
            set;
        }

        public String Url
        {
            get;
            set;
        }
    }
}